package gr.archimedesai.distributed.hash;

import gr.archimedesai.Pair;
import gr.archimedesai.algorithms.Algorithms;
import gr.archimedesai.distributed.SparkLogParser;
import gr.archimedesai.distributed.grid.Grid;
import gr.archimedesai.distributed.partitioner.CustomPartitioner;
import gr.archimedesai.shapes.Point;
import gr.archimedesai.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {

        SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{Object.class, Grid.class, HashMap.class, Point.class, Rectangle.class, Pair.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class,org.apache.spark.util.collection.CompactBuffer.class,Class.forName("scala.reflect.ManifestFactory$ObjectManifest")/*,scala.reflect.ManifestFactory$.MODULE$.Any().getClass()*/});
        SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[2]")*/.getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
        CustomPartitioner partitioner = new CustomPartitioner(Integer.parseInt(args[11]));

        final int xIndex = Integer.parseInt(args[1]);
        final int yIndex = Integer.parseInt(args[2]);

        try {
            File file = new File(args[10]);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            long t1 = System.currentTimeMillis();

            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(Double.parseDouble(args[4]),Double.parseDouble(args[5])), Point.newPoint(Double.parseDouble(args[6]),Double.parseDouble(args[7]))), Integer.parseInt(args[8]), Integer.parseInt(args[9]));

            Broadcast<Grid> gridBroadcasted = jsc.broadcast(grid);
            JavaRDD<String> rddString = jsc.textFile(args[0]);

    //        JavaRDD<String> rddString = jsc.textFile(args[2] + args[3] + ".csv");

            JavaRDD<Pair> rddData = rddString.map((String line) -> {
                String[] elements = line.split(args[3]);
                double x = Double.parseDouble(elements[xIndex]);
                double y = Double.parseDouble(elements[yIndex]);
                return Pair.newPair(x,y);
            }).persist(StorageLevel.MEMORY_ONLY());

            long[] concDiscStripesY = rddData.mapToPair(t->{
                Grid gridBr = gridBroadcasted.getValue();
                return new Tuple2<>(gridBr.getYStripeId(t.getY()), t);
            }).groupByKey(partitioner).map(tup->{

                int counter = 0;
                Iterator<Pair> it = tup._2.iterator();
                while (it.hasNext()) {
                    it.next();
                    counter++;
                }

                it = tup._2.iterator();
                Pair[] points = new Pair[counter];
                counter = 0;

                while (it.hasNext()) {
                    points[counter] = it.next();
                    counter++;
                }
                Algorithms.sortPairsByX(points);
                return Algorithms.apacheCommons(points)._2;
            }).reduce((Function2<long[], long[], long[]>) (o1, o2) -> {
                o1[0]= o1[0]+o2[0];
                o1[1]= o1[1]+o2[1];
                o1[2]= o1[2]+o2[2];
                o1[3]= o1[3]+o2[3];
                return o1;
            });
            System.out.println(Arrays.toString(concDiscStripesY));

            Tuple2<long[],HashMap<Integer,Integer>> concDiscStripesX = rddData.mapToPair(t->{
                Grid gridBr = gridBroadcasted.getValue();
                return new Tuple2<>(gridBr.getXStripeId(t.getX()), t);
            }).groupByKey(partitioner).map(tup->{

                Grid gridBr = gridBroadcasted.getValue();

                int[] elementsPerCell = new int[(int)gridBr.getCellsInYAxis()];

                Iterator<Pair> it = tup._2.iterator();
                while (it.hasNext()) {
                    Pair t = it.next();
                    elementsPerCell[gridBr.getYStripeId(t.getY())]++;
                }

                int nonNullCells = 0;
                for (int i : elementsPerCell) {
                    if(i!=0){
                        nonNullCells++;
                    }
                }

                HashMap<Integer,Pair[]> map = new HashMap<>((int)Math.ceil(nonNullCells / 0.75));
                for (int i = 0; i < elementsPerCell.length; i++) {
                    if(elementsPerCell[i]!=0){
                        map.put(i, new Pair[elementsPerCell[i]]);
                        elementsPerCell[i] = 0;
                    }
                }

                it = tup._2.iterator();
                while (it.hasNext()) {
                    Pair t = it.next();
                    int stripeId = gridBr.getYStripeId(t.getY());
                    map.get(stripeId)[elementsPerCell[stripeId]] = t;
                    elementsPerCell[stripeId]++;
                }


                map.forEach((k,v)->{
                    Arrays.sort(v, Comparator.comparing(Pair::getX));
                });

                long[] numbers = new long[4];
                for (int i = elementsPerCell.length - 1; i > 0; i--) {
                    if(elementsPerCell[i]!=0){
                        Pair[] current = map.get(i);
                        for (int j = i - 1; j >= 0; j--) {
                            if(elementsPerCell[j]!=0){
                                long[] numbersFromTwoCells = Algorithms.southTile(current, map.get(j));
                                numbers[0] = numbers[0] + numbersFromTwoCells[0];
                                numbers[1] = numbers[1] + numbersFromTwoCells[1];
    //                            numbers[2] = numbers[2] + numbersFromTwoCells[2];
                            }
                        }
                    }
                }
                HashMap<Integer,Integer> cellHistogram = new HashMap<>(map.size());
                map.forEach((k,v)->{
                    cellHistogram.put(gridBr.getCellIdFromXcYc(tup._1,k), v.length);
                });

                return new Tuple2<>(numbers, cellHistogram);
            }).reduce((Function2<Tuple2<long[], HashMap<Integer, Integer>>, Tuple2<long[], HashMap<Integer, Integer>>, Tuple2<long[], HashMap<Integer, Integer>>>) (tuple1, tuple2) -> {
                tuple1._1[0] = tuple1._1[0]+tuple2._1[0];
                tuple1._1[1] = tuple1._1[1]+tuple2._1[1];
    //            tuple1._1[2] = tuple1._1[2]+tuple2._1[2];
                tuple1._2.putAll(tuple2._2);
                return tuple1;
            });

            long t2 = System.currentTimeMillis();
            concDiscStripesY[0] = concDiscStripesY[0] + concDiscStripesX._1[0];
            concDiscStripesY[1] = concDiscStripesY[1] + concDiscStripesX._1[1];

//            for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
//                for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
//                    int c = (concDiscStripesX._2.getOrDefault((grid.getCellIdFromXcYc(xc, yc)),-1));
//                    if(c!=-1){
//                        long cDisc = 0;
//
//                        for (int xcDisc = xc+1; xcDisc < grid.getCellsInXAxis(); xcDisc++) {
//                            for (int ycDisc = 0; ycDisc < yc; ycDisc++) {
//                                int c2 = (concDiscStripesX._2.getOrDefault((grid.getCellIdFromXcYc(xcDisc, ycDisc)),-1));
//                                if(c2!=-1){
//                                    cDisc = cDisc + c2;
//                                }
//                            }
//                        }
//                        concDiscStripesY[0] = concDiscStripesY[0] + cDisc*c;
//                    }
//                }
//            }
            concDiscStripesY[0] = concDiscStripesY[0] + Algorithms.discordantCells(concDiscStripesX._2, grid.getCellsInXAxis(), grid.getCellsInYAxis());

            long lineCount = 0;
            for (int value : concDiscStripesX._2.values()) {
                lineCount = lineCount + value;
            }
            long t3 = System.currentTimeMillis();

            long concordants = ((lineCount*(lineCount-1))/2)-concDiscStripesY[0]-concDiscStripesY[1]-concDiscStripesY[2]-concDiscStripesY[3];
            double tau = (concordants-concDiscStripesY[0])/Math.sqrt((double)(concordants+concDiscStripesY[0]+concDiscStripesY[1])*(concordants+concDiscStripesY[0]+concDiscStripesY[2]));
            long elapsedtime = ((System.currentTimeMillis() - t1) / 1000);

            System.out.println(Arrays.toString(concDiscStripesY));
            System.out.println("tau is: " + tau);
            System.out.println(elapsedtime);


            jsc.close();
            sparkSession.close();

            Path directory = Paths.get(System.getProperty("user.home"));
            String pattern = "application_";
            String sparkLogValues ="";
            String sparkLogMb = "";
            try (Stream<Path> files = Files.list(directory)) {
                List<Path> paths = files.filter(path -> path.getFileName().toString().startsWith(pattern)).collect(Collectors.toList());
                if(paths.isEmpty()){
                    throw new Exception("The are other files starting with 'application_' in the path");
                }else if(paths.size()>1){
                    throw new Exception("The are other files starting with 'application_' in the path");
                }
                sparkLogValues = SparkLogParser.getTimeFromStates(paths.get(0).toAbsolutePath().toString());
                sparkLogMb = SparkLogParser.getProperty(paths.get(0).toAbsolutePath().toString(), "\"Remote Bytes Read\"");
                paths.get(0).toFile().delete();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Path path = Paths.get(args[0]);
            BufferedWriter bwCellsX = new BufferedWriter(new FileWriter("stripes-gridRegular-"+grid.getCellsInXAxis()+"-"+grid.getCellsInYAxis()+"-"+"X-"+path.getFileName()));
            BufferedWriter bwCellsY = new BufferedWriter(new FileWriter("stripes-gridRegular-"+grid.getCellsInXAxis()+"-"+grid.getCellsInYAxis()+"-"+"Y-"+path.getFileName()));


            for (int xc = 0; xc < grid.getCellsInXAxis(); xc++) {
                int pointsInX = 0;
                for (int yc = 0; yc < grid.getCellsInYAxis(); yc++) {
                    pointsInX = pointsInX + (concDiscStripesX._2.getOrDefault((grid.getCellIdFromXcYc(xc, yc)),0));
                }
                bwCellsX.write(xc+"\t"+"["+grid.getXLowerRangeByStripeId(xc)+", "+grid.getXUpperRangeByStripeId(xc)+")"+"\t"+pointsInX+"\n");
            }

            for (int yc = 0; yc < grid.getCellsInYAxis(); yc++) {
                int pointsInY = 0;
                for (int xc = 0; xc < grid.getCellsInXAxis(); xc++) {
                    pointsInY = pointsInY + (concDiscStripesX._2.getOrDefault((grid.getCellIdFromXcYc(xc, yc)),0));
                }
                bwCellsY.write(yc+"\t"+"["+grid.getYLowerRangeByStripeId(yc)+", "+grid.getYUpperRangeByStripeId(yc)+")"+"\t"+pointsInY+"\n");
            }
            bwCellsX.close();
            bwCellsY.close();

            bw.write(Integer.parseInt(args[8])+","+elapsedtime+","+sparkLogValues+","+((t3-t2)/1000)+","+sparkLogMb+"\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}