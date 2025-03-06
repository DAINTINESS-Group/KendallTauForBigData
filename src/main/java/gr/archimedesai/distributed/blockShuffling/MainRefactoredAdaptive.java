package gr.archimedesai.distributed.blockShuffling;

import gr.archimedesai.Pair;
import gr.archimedesai.algorithms.Algorithms;
import gr.archimedesai.distributed.grid.AdaptiveGrid;
import gr.archimedesai.distributed.partitioner.CustomPartitioner;
import gr.archimedesai.shapes.Point;
import gr.archimedesai.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.io.*;
import java.util.*;

public class MainRefactoredAdaptive {
    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{Object.class,AdaptiveGrid.class, HashMap.class, Point.class, Rectangle.class, Pair.class, Pair[].class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class,org.apache.spark.util.collection.CompactBuffer.class/*,scala.reflect.ManifestFactory$.MODULE$.Any().getClass()*/});
        SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[2]")*/.getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
        CustomPartitioner partitioner = new CustomPartitioner(Integer.parseInt(args[11]));

        final int xIndex = Integer.parseInt(args[1]);
        final int yIndex = Integer.parseInt(args[2]);
        final String sampleFilePath = args[12];

        try {
                File file = new File(args[10]);
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);

            long t1 = System.currentTimeMillis();

            List<Pair> samplesPairs = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(sampleFilePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] vals = line.split(args[3]);
                    samplesPairs.add(Pair.newPair(Double.parseDouble(vals[xIndex]), Double.parseDouble(vals[yIndex])));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            AdaptiveGrid grid = AdaptiveGrid.newAdaptiveGrid(Rectangle.newRectangle(Point.newPoint(Double.parseDouble(args[4]),Double.parseDouble(args[5])), Point.newPoint(Double.parseDouble(args[6]),Double.parseDouble(args[7]))), Integer.parseInt(args[8]), Integer.parseInt(args[9]),samplesPairs);

            Broadcast<AdaptiveGrid> gridBroadcasted = jsc.broadcast(grid);
            JavaRDD<String> rddString = jsc.textFile(args[0]);

    //        JavaRDD<String> rddString = jsc.textFile(args[2] + args[3] + ".csv");

            JavaRDD<Pair> rddData = rddString.map((String line) -> {
                String[] elements = line.split(args[3]);
                double x = Double.parseDouble(elements[xIndex]);
                double y = Double.parseDouble(elements[yIndex]);
                return Pair.newPair(x,y);
            });

            JavaRDD<Tuple2<Collection<Tuple2<Integer,Pair[]>>,long[]>> rddYStripes = rddData.mapToPair(t->{
                AdaptiveGrid gridBr = gridBroadcasted.getValue();
                return new Tuple2<>(gridBr.getYStripeId(t.getY()), t);
            }).groupByKey(partitioner).map(tup->{

                int[] elementsPerCell = new int[gridBroadcasted.getValue().getCellsInXAxis()];

                int counter = 0;
                Iterator<Pair> it = tup._2.iterator();
                while (it.hasNext()) {
                    Pair pair = it.next();
                    elementsPerCell[gridBroadcasted.getValue().getXStripeId(pair.getX())]++;
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
                Tuple2<Pair[],long[]> tuple2 = Algorithms.apacheCommons(points);

                long[] concDiscStripesY = tuple2._2;
                points = tuple2._1;

                int distinctCells = 0;
                for (int i : elementsPerCell) {
                    if(i!=0){
                        distinctCells++;
                    }
                }
                HashMap<Integer,Tuple2<Integer, Pair[]>> cells = new HashMap<>((int)Math.ceil(distinctCells / 0.75));

                for (int i = 0; i < elementsPerCell.length; i++) {
                    if(elementsPerCell[i]!=0){
                        cells.put(i, Tuple2.apply(i,new Pair[elementsPerCell[i]]));
                        elementsPerCell[i] = 0;
                    }
                }

                for (Pair point : points) {
                    int i = gridBroadcasted.getValue().getXStripeId(point.getX());
                    Pair[] pair = cells.get(i)._2;
                    pair[elementsPerCell[i]++] = point;
                }

                cells.forEach((k,v)->{
                    Arrays.sort(v._2, new Comparator<Pair>() {
                        /** {@inheritDoc} */
                        @Override
                        public int compare(Pair pair1, Pair pair2) {
                            int compareFirst = Double.compare(pair1.getX(),pair2.getX());
                            return compareFirst != 0 ? compareFirst : Double.compare(pair1.getY(),pair2.getY());
                        }
                    });
                });

                return Tuple2.apply(cells.values(),concDiscStripesY);
            });//.persist(StorageLevel.MEMORY_ONLY());

            long[] concDiscStripesY = rddYStripes.map(o->o._2).reduce((o1, o2) -> {
                o1[0]= o1[0]+o2[0];
                o1[1]= o1[1]+o2[1];
                o1[2]= o1[2]+o2[2];
                o1[3]= o1[3]+o2[3];
                return o1;
            });

            System.out.println(Arrays.toString(concDiscStripesY));

            Tuple2<long[],HashMap<Integer,Integer>> concDiscStripesX = rddYStripes.flatMapToPair(o->o._1.iterator())
                    .groupByKey(partitioner).map(tup->{

                int numCells = 0;
                Iterator<Pair[]> it = tup._2.iterator();
                while (it.hasNext()) {
                    it.next();
                    numCells++;
                }

                Pair[][] cells = new Pair[numCells][];
                numCells = 0;
                it = tup._2.iterator();
                while (it.hasNext()) {
                    cells[numCells] = it.next();
                    numCells++;
                }

                Arrays.sort(cells, new Comparator<Pair[]>() {
                    /** {@inheritDoc} */
                    @Override
                    public int compare(Pair[] pairs1, Pair[] pairs2) {
                        return Double.compare(pairs2[0].getY(),pairs1[0].getY());
                    }
                });

                long[] numbers = new long[2];

                for (int i = 0; i < cells.length-1; i++) {
                    for (int j = i+1; j < cells.length; j++) {
                        long[] numbersFromTwoCells = Algorithms.southTile(cells[i], cells[j]);
                        numbers[0] = numbers[0] + numbersFromTwoCells[0];
                        numbers[1] = numbers[1] + numbersFromTwoCells[1];
    //                    numbers[2] = numbers[2] + numbersFromTwoCells[2];
                    }
                }

                HashMap<Integer,Integer> cellHistogram = new HashMap<>((int)Math.ceil(cells.length / 0.75));
                for (Pair[] cell : cells) {
                    cellHistogram.put(gridBroadcasted.getValue().getCellIdFromXcYc(tup._1,gridBroadcasted.getValue().getYStripeId(cell[0].getY())), cell.length);
                }
                return new Tuple2<>(numbers, cellHistogram);
            }).reduce((Function2<Tuple2<long[], HashMap<Integer, Integer>>, Tuple2<long[], HashMap<Integer, Integer>>, Tuple2<long[], HashMap<Integer, Integer>>>) (tuple1, tuple2) -> {
                tuple1._1[0] = tuple1._1[0]+tuple2._1[0];
                tuple1._1[1] = tuple1._1[1]+tuple2._1[1];
    //            tuple1._1[2] = tuple1._1[2]+tuple2._1[2];
                tuple1._2.putAll(tuple2._2);
                return tuple1;
            });

            concDiscStripesY[0] = concDiscStripesY[0] + concDiscStripesX._1[0];
            concDiscStripesY[1] = concDiscStripesY[1] + concDiscStripesX._1[1];
    //        concDiscStripesY[2] = concDiscStripesY[2] + concDiscStripesX._1[2];


            for (int xc = grid.getCellsInXAxis()-1; xc >= 0; xc--) {
                for (int yc = grid.getCellsInYAxis()-1; yc >= 0; yc--) {
                    int c = (concDiscStripesX._2.getOrDefault((grid.getCellIdFromXcYc(xc, yc)),-1));
                    if(c!=-1){
    //                    long cConc = 0;
                        long cDisc = 0;
    //                    for (int xcConc = 0; xcConc < xc; xcConc++) {
    //                        for (int ycConc = 0; ycConc < yc; ycConc++) {
    //                            int c1 = (concDiscStripesX._2.getOrDefault((grid.getCellIdFromXcYc(xcConc, ycConc)),-1));
    //                            if(c1!=-1){
    //                                cConc = cConc + c1;
    //                            }
    //                        }
    //                    }

                        for (int xcDisc = xc+1; xcDisc < grid.getCellsInXAxis(); xcDisc++) {
                            for (int ycDisc = 0; ycDisc < yc; ycDisc++) {
                                int c2 = (concDiscStripesX._2.getOrDefault((grid.getCellIdFromXcYc(xcDisc, ycDisc)),-1));
                                if(c2!=-1){
                                    cDisc = cDisc + c2;
                                }
                            }
                        }
    //                    concDiscStripesY[0] = concDiscStripesY[0] + cConc*c;
                        concDiscStripesY[0] = concDiscStripesY[0] + cDisc*c;
                    }
                }
            }

            long lineCount = 0;
            for (int value : concDiscStripesX._2.values()) {
                lineCount = lineCount + value;
            }

            long concordants = ((lineCount*(lineCount-1))/2)-concDiscStripesY[0]-concDiscStripesY[1]-concDiscStripesY[2]-concDiscStripesY[3];
            double tau = (concordants-concDiscStripesY[0])/Math.sqrt((double)(concordants+concDiscStripesY[0]+concDiscStripesY[1])*(concordants+concDiscStripesY[0]+concDiscStripesY[2]));
            long elapsedtime = ((System.currentTimeMillis() - t1) / 1000);

            System.out.println(Arrays.toString(concDiscStripesY));
            System.out.println("tau is: " + tau);
            System.out.println(elapsedtime);

            bw.write(Integer.parseInt(args[8])+","+elapsedtime+"\n");
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        jsc.close();
        sparkSession.close();
    }
}