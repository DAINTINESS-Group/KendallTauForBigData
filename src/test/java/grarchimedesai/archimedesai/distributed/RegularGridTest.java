package grarchimedesai.archimedesai.distributed;

import grarchimedesai.archimedesai.Pair;
import grarchimedesai.archimedesai.algorithms.Algorithms;
import grarchimedesai.archimedesai.centralized.grid.Cell;
import grarchimedesai.archimedesai.distributed.grid.Grid;
import grarchimedesai.archimedesai.distributed.partitioner.CustomPartitioner;
import grarchimedesai.archimedesai.shapes.Point;
import grarchimedesai.archimedesai.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import org.junit.jupiter.api.Test;
import scala.Tuple2;
import scala.Tuple3;

import java.util.*;


class RegularGridTest {
    @Test
    void regularGridGaussianSet() throws ClassNotFoundException {

        final int sparkPartitions = 16;
        final String filePath = "./src/main/resources/gaussian.csv";
        final int xIndex = 0;
        final int yIndex = 1;
        final int cellsInXAxis = 50;
        final int cellsInYAxis = 50;
        final String delimiter =",";

        final double minX = 0;
        final double minY = 0;
        final double maxX = 1;
        final double maxY = 1;

        SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{Object.class, grarchimedesai.archimedesai.distributed.grid.Grid.class, HashMap.class, Point.class, Rectangle.class, Pair.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class,org.apache.spark.util.collection.CompactBuffer.class,Class.forName("scala.reflect.ManifestFactory$ObjectManifest")/*,scala.reflect.ManifestFactory$.MODULE$.Any().getClass()*/});
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).master("local[*]").getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
        CustomPartitioner partitioner = new CustomPartitioner(sparkPartitions);

            long t1 = System.currentTimeMillis();

            Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(minX,minY), Point.newPoint(maxX,maxY)), cellsInXAxis, cellsInYAxis);

            Broadcast<Grid> gridBroadcasted = jsc.broadcast(grid);
            JavaRDD<String> rddString = jsc.textFile(filePath);

            JavaRDD<Pair> rddData = rddString.map((String line) -> {
                String[] elements = line.split(delimiter);
                double x = Double.parseDouble(elements[xIndex]);
                double y = Double.parseDouble(elements[yIndex]);
                return Pair.newPair(x,y);
            }).persist(StorageLevel.MEMORY_ONLY());

            long[] concDiscStripesY = rddData.mapToPair(t->{
                grarchimedesai.archimedesai.distributed.grid.Grid gridBr = gridBroadcasted.getValue();
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


            System.out.println("Data loading + Knight processing on stripes: "+ (System.currentTimeMillis()-t1) +" ms");

            long t2 = System.currentTimeMillis();
            List<Tuple3<Integer, long[], int[]>> concDiscStripesX = rddData.mapToPair(t->{
                grarchimedesai.archimedesai.distributed.grid.Grid gridBr = gridBroadcasted.getValue();
                return new Tuple2<>(gridBr.getXStripeId(t.getX()), t);
            }).groupByKey(partitioner).map(tup->{

                Grid gridBr = gridBroadcasted.getValue();

                int[] elementsPerCell = new int[(int)gridBr.getCellsInYAxis()];

                Iterator<Pair> it = tup._2.iterator();
                while (it.hasNext()) {
                    Pair t = it.next();
                    elementsPerCell[gridBr.getYStripeId(t.getY())]++;
                }

                Cell[] cells = new Cell[gridBr.getCellsInYAxis()];
                for (int i = 0; i < elementsPerCell.length; i++) {
                    if(elementsPerCell[i]!=0){
                        cells[i] = Cell.newCell(elementsPerCell[i]);
                        elementsPerCell[i] = 0;
                    }
                }

                it = tup._2.iterator();
                while (it.hasNext()) {
                    Pair t = it.next();
                    int stripeId = gridBr.getYStripeId(t.getY());
                    cells[stripeId].getPairs()[elementsPerCell[stripeId]]=t;
                    elementsPerCell[stripeId]++;
                }

                for (int i = 0; i < cells.length; i++) {
                    if(cells[i]!=null){
                        Arrays.sort(cells[i].getPairs(), Comparator.comparing(Pair::getX));
                    }
                }

                long[] numbers = new long[2];
                for (int i = elementsPerCell.length - 1; i > 0; i--) {
                    if(elementsPerCell[i]!=0){
                        Pair[] current = cells[i].getPairs();
                        for (int j = i - 1; j >= 0; j--) {
                            if(elementsPerCell[j]!=0){
                                long[] numbersFromTwoCells = Algorithms.southTile(current, cells[j].getPairs());
                                numbers[0] = numbers[0] + numbersFromTwoCells[0];
                                numbers[1] = numbers[1] + numbersFromTwoCells[1];
                            }
                        }
                    }
                }

                return new Tuple3<>(tup._1,numbers, elementsPerCell);
            }).collect();

            System.out.println("South tiles processing: " + (System.currentTimeMillis()-t2) + " ms");

            long t3 = System.currentTimeMillis();
            long lineCount = 0;
            int[][] histogram = new int[grid.getCellsInXAxis()][grid.getCellsInYAxis()];
            for (Tuple3<Integer, long[], int[]> discStripesX : concDiscStripesX) {
                concDiscStripesY[0] = concDiscStripesY[0] + discStripesX._2()[0];
                concDiscStripesY[1] = concDiscStripesY[1] + discStripesX._2()[1];
                int columnid = discStripesX._1();
                int[] rows = discStripesX._3();
                for (int i = 0; i < discStripesX._3().length; i++) {
                    lineCount = lineCount + rows[i];
                    histogram[columnid][i] = rows[i];
                }
            }
            concDiscStripesY[0] = concDiscStripesY[0] + Algorithms.discordantCells(histogram, grid.getCellsInXAxis(), grid.getCellsInYAxis());
            System.out.println("Histogram processing: " + (System.currentTimeMillis()-t3) + " ms");

            long concordants = ((lineCount*(lineCount-1))/2)-concDiscStripesY[0]-concDiscStripesY[1]-concDiscStripesY[2]-concDiscStripesY[3];
            double tau = (concordants-concDiscStripesY[0])/Math.sqrt((double)(concordants+concDiscStripesY[0]+concDiscStripesY[1])*(concordants+concDiscStripesY[0]+concDiscStripesY[2]));
            long elapsedtime = ((System.currentTimeMillis() - t1));

            System.out.println("tau is: " + tau);
            System.out.println("Elapsed time: "+elapsedtime+" ms");

            jsc.close();
            sparkSession.close();
    }

    @Test
    void regularGridSierpinskiSet() throws ClassNotFoundException {

        final int sparkPartitions = 16;
        final String filePath = "./src/main/resources/sierpinski.csv";
        final int xIndex = 0;
        final int yIndex = 1;
        final int cellsInXAxis = 50;
        final int cellsInYAxis = 50;
        final String delimiter =",";

        final double minX = 0;
        final double minY = 0;
        final double maxX = 1;
        final double maxY = 1;

        SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{Object.class, grarchimedesai.archimedesai.distributed.grid.Grid.class, HashMap.class, Point.class, Rectangle.class, Pair.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class,org.apache.spark.util.collection.CompactBuffer.class,Class.forName("scala.reflect.ManifestFactory$ObjectManifest")/*,scala.reflect.ManifestFactory$.MODULE$.Any().getClass()*/});
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).master("local[*]").getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
        CustomPartitioner partitioner = new CustomPartitioner(sparkPartitions);

        long t1 = System.currentTimeMillis();

        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(minX,minY), Point.newPoint(maxX,maxY)), cellsInXAxis, cellsInYAxis);

        Broadcast<Grid> gridBroadcasted = jsc.broadcast(grid);
        JavaRDD<String> rddString = jsc.textFile(filePath);

        JavaRDD<Pair> rddData = rddString.map((String line) -> {
            String[] elements = line.split(delimiter);
            double x = Double.parseDouble(elements[xIndex]);
            double y = Double.parseDouble(elements[yIndex]);
            return Pair.newPair(x,y);
        }).persist(StorageLevel.MEMORY_ONLY());

        long[] concDiscStripesY = rddData.mapToPair(t->{
            grarchimedesai.archimedesai.distributed.grid.Grid gridBr = gridBroadcasted.getValue();
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


        System.out.println("Data loading + Knight processing on stripes: "+ (System.currentTimeMillis()-t1) +" ms");

        long t2 = System.currentTimeMillis();
        List<Tuple3<Integer, long[], int[]>> concDiscStripesX = rddData.mapToPair(t->{
            grarchimedesai.archimedesai.distributed.grid.Grid gridBr = gridBroadcasted.getValue();
            return new Tuple2<>(gridBr.getXStripeId(t.getX()), t);
        }).groupByKey(partitioner).map(tup->{

            Grid gridBr = gridBroadcasted.getValue();

            int[] elementsPerCell = new int[(int)gridBr.getCellsInYAxis()];

            Iterator<Pair> it = tup._2.iterator();
            while (it.hasNext()) {
                Pair t = it.next();
                elementsPerCell[gridBr.getYStripeId(t.getY())]++;
            }

            Cell[] cells = new Cell[gridBr.getCellsInYAxis()];
            for (int i = 0; i < elementsPerCell.length; i++) {
                if(elementsPerCell[i]!=0){
                    cells[i] = Cell.newCell(elementsPerCell[i]);
                    elementsPerCell[i] = 0;
                }
            }

            it = tup._2.iterator();
            while (it.hasNext()) {
                Pair t = it.next();
                int stripeId = gridBr.getYStripeId(t.getY());
                cells[stripeId].getPairs()[elementsPerCell[stripeId]]=t;
                elementsPerCell[stripeId]++;
            }

            for (int i = 0; i < cells.length; i++) {
                if(cells[i]!=null){
                    Arrays.sort(cells[i].getPairs(), Comparator.comparing(Pair::getX));
                }
            }

            long[] numbers = new long[2];
            for (int i = elementsPerCell.length - 1; i > 0; i--) {
                if(elementsPerCell[i]!=0){
                    Pair[] current = cells[i].getPairs();
                    for (int j = i - 1; j >= 0; j--) {
                        if(elementsPerCell[j]!=0){
                            long[] numbersFromTwoCells = Algorithms.southTile(current, cells[j].getPairs());
                            numbers[0] = numbers[0] + numbersFromTwoCells[0];
                            numbers[1] = numbers[1] + numbersFromTwoCells[1];
                        }
                    }
                }
            }

            return new Tuple3<>(tup._1,numbers, elementsPerCell);
        }).collect();

        System.out.println("South tiles processing: " + (System.currentTimeMillis()-t2) + " ms");

        long t3 = System.currentTimeMillis();
        long lineCount = 0;
        int[][] histogram = new int[grid.getCellsInXAxis()][grid.getCellsInYAxis()];
        for (Tuple3<Integer, long[], int[]> discStripesX : concDiscStripesX) {
            concDiscStripesY[0] = concDiscStripesY[0] + discStripesX._2()[0];
            concDiscStripesY[1] = concDiscStripesY[1] + discStripesX._2()[1];
            int columnid = discStripesX._1();
            int[] rows = discStripesX._3();
            for (int i = 0; i < discStripesX._3().length; i++) {
                lineCount = lineCount + rows[i];
                histogram[columnid][i] = rows[i];
            }
        }
        concDiscStripesY[0] = concDiscStripesY[0] + Algorithms.discordantCells(histogram, grid.getCellsInXAxis(), grid.getCellsInYAxis());
        System.out.println("Histogram processing: " + (System.currentTimeMillis()-t3) + " ms");

        long concordants = ((lineCount*(lineCount-1))/2)-concDiscStripesY[0]-concDiscStripesY[1]-concDiscStripesY[2]-concDiscStripesY[3];
        double tau = (concordants-concDiscStripesY[0])/Math.sqrt((double)(concordants+concDiscStripesY[0]+concDiscStripesY[1])*(concordants+concDiscStripesY[0]+concDiscStripesY[2]));
        long elapsedtime = ((System.currentTimeMillis() - t1));

        System.out.println("tau is: " + tau);
        System.out.println("Elapsed time: "+elapsedtime+" ms");

        jsc.close();
        sparkSession.close();
    }

    @Test
    void regularGridTaxiSet() throws ClassNotFoundException {

        final int sparkPartitions = 16;
        final String filePath = "./src/main/resources/taxi.csv";
        final int xIndex = 0;
        final int yIndex = 2;
        final int cellsInXAxis = 50;
        final int cellsInYAxis = 50;
        final String delimiter =",";

        final double minX = 0;
        final double minY = 0;
        final double maxX = 6.40172993E8;
        final double maxY = 861611.06;

        SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{Object.class, grarchimedesai.archimedesai.distributed.grid.Grid.class, HashMap.class, Point.class, Rectangle.class, Pair.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class,org.apache.spark.util.collection.CompactBuffer.class,Class.forName("scala.reflect.ManifestFactory$ObjectManifest")/*,scala.reflect.ManifestFactory$.MODULE$.Any().getClass()*/});
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).master("local[*]").getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
        CustomPartitioner partitioner = new CustomPartitioner(sparkPartitions);

        long t1 = System.currentTimeMillis();

        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(minX,minY), Point.newPoint(maxX,maxY)), cellsInXAxis, cellsInYAxis);

        Broadcast<Grid> gridBroadcasted = jsc.broadcast(grid);
        JavaRDD<String> rddString = jsc.textFile(filePath);

        JavaRDD<Pair> rddData = rddString.map((String line) -> {
            String[] elements = line.split(delimiter);
            double x = Double.parseDouble(elements[xIndex]);
            double y = Double.parseDouble(elements[yIndex]);
            return Pair.newPair(x,y);
        }).persist(StorageLevel.MEMORY_ONLY());

        long[] concDiscStripesY = rddData.mapToPair(t->{
            grarchimedesai.archimedesai.distributed.grid.Grid gridBr = gridBroadcasted.getValue();
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


        System.out.println("Data loading + Knight processing on stripes: "+ (System.currentTimeMillis()-t1) +" ms");

        long t2 = System.currentTimeMillis();
        List<Tuple3<Integer, long[], int[]>> concDiscStripesX = rddData.mapToPair(t->{
            grarchimedesai.archimedesai.distributed.grid.Grid gridBr = gridBroadcasted.getValue();
            return new Tuple2<>(gridBr.getXStripeId(t.getX()), t);
        }).groupByKey(partitioner).map(tup->{

            Grid gridBr = gridBroadcasted.getValue();

            int[] elementsPerCell = new int[(int)gridBr.getCellsInYAxis()];

            Iterator<Pair> it = tup._2.iterator();
            while (it.hasNext()) {
                Pair t = it.next();
                elementsPerCell[gridBr.getYStripeId(t.getY())]++;
            }

            Cell[] cells = new Cell[gridBr.getCellsInYAxis()];
            for (int i = 0; i < elementsPerCell.length; i++) {
                if(elementsPerCell[i]!=0){
                    cells[i] = Cell.newCell(elementsPerCell[i]);
                    elementsPerCell[i] = 0;
                }
            }

            it = tup._2.iterator();
            while (it.hasNext()) {
                Pair t = it.next();
                int stripeId = gridBr.getYStripeId(t.getY());
                cells[stripeId].getPairs()[elementsPerCell[stripeId]]=t;
                elementsPerCell[stripeId]++;
            }

            for (int i = 0; i < cells.length; i++) {
                if(cells[i]!=null){
                    Arrays.sort(cells[i].getPairs(), Comparator.comparing(Pair::getX));
                }
            }

            long[] numbers = new long[2];
            for (int i = elementsPerCell.length - 1; i > 0; i--) {
                if(elementsPerCell[i]!=0){
                    Pair[] current = cells[i].getPairs();
                    for (int j = i - 1; j >= 0; j--) {
                        if(elementsPerCell[j]!=0){
                            long[] numbersFromTwoCells = Algorithms.southTile(current, cells[j].getPairs());
                            numbers[0] = numbers[0] + numbersFromTwoCells[0];
                            numbers[1] = numbers[1] + numbersFromTwoCells[1];
                        }
                    }
                }
            }

            return new Tuple3<>(tup._1,numbers, elementsPerCell);
        }).collect();

        System.out.println("South tiles processing: " + (System.currentTimeMillis()-t2) + " ms");

        long t3 = System.currentTimeMillis();
        long lineCount = 0;
        int[][] histogram = new int[grid.getCellsInXAxis()][grid.getCellsInYAxis()];
        for (Tuple3<Integer, long[], int[]> discStripesX : concDiscStripesX) {
            concDiscStripesY[0] = concDiscStripesY[0] + discStripesX._2()[0];
            concDiscStripesY[1] = concDiscStripesY[1] + discStripesX._2()[1];
            int columnid = discStripesX._1();
            int[] rows = discStripesX._3();
            for (int i = 0; i < discStripesX._3().length; i++) {
                lineCount = lineCount + rows[i];
                histogram[columnid][i] = rows[i];
            }
        }
        concDiscStripesY[0] = concDiscStripesY[0] + Algorithms.discordantCells(histogram, grid.getCellsInXAxis(), grid.getCellsInYAxis());
        System.out.println("Histogram processing: " + (System.currentTimeMillis()-t3) + " ms");

        long concordants = ((lineCount*(lineCount-1))/2)-concDiscStripesY[0]-concDiscStripesY[1]-concDiscStripesY[2]-concDiscStripesY[3];
        double tau = (concordants-concDiscStripesY[0])/Math.sqrt((double)(concordants+concDiscStripesY[0]+concDiscStripesY[1])*(concordants+concDiscStripesY[0]+concDiscStripesY[2]));
        long elapsedtime = ((System.currentTimeMillis() - t1));

        System.out.println("tau is: " + tau);
        System.out.println("Elapsed time: "+elapsedtime+" ms");

        jsc.close();
        sparkSession.close();

    }

    @Test
    void regularGridRadiationSet() throws ClassNotFoundException {

        final int sparkPartitions = 16;
        final String filePath = "./src/main/resources/radiation.csv";
        final int xIndex = 0;
        final int yIndex = 2;
        final int cellsInXAxis = 50;
        final int cellsInYAxis = 50;
        final String delimiter =",";

        final double minX = 3.5;
        final double minY = 1.0;
        final double maxX = 90.1;
        final double maxY = 1351.1;

        SparkConf sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "true")
                .registerKryoClasses(new Class[]{Object.class, grarchimedesai.archimedesai.distributed.grid.Grid.class, HashMap.class, Point.class, Rectangle.class, Pair.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class,org.apache.spark.util.collection.CompactBuffer.class,Class.forName("scala.reflect.ManifestFactory$ObjectManifest")/*,scala.reflect.ManifestFactory$.MODULE$.Any().getClass()*/});
        SparkSession sparkSession = SparkSession.builder().config(sparkConf).master("local[*]").getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
        CustomPartitioner partitioner = new CustomPartitioner(sparkPartitions);

        long t1 = System.currentTimeMillis();

        Grid grid = Grid.newGrid(Rectangle.newRectangle(Point.newPoint(minX,minY), Point.newPoint(maxX,maxY)), cellsInXAxis, cellsInYAxis);

        Broadcast<Grid> gridBroadcasted = jsc.broadcast(grid);
        JavaRDD<String> rddString = jsc.textFile(filePath);

        JavaRDD<Pair> rddData = rddString.map((String line) -> {
            String[] elements = line.split(delimiter);
            double x = Double.parseDouble(elements[xIndex]);
            double y = Double.parseDouble(elements[yIndex]);
            return Pair.newPair(x,y);
        }).persist(StorageLevel.MEMORY_ONLY());

        long[] concDiscStripesY = rddData.mapToPair(t->{
            grarchimedesai.archimedesai.distributed.grid.Grid gridBr = gridBroadcasted.getValue();
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


        System.out.println("Data loading + Knight processing on stripes: "+ (System.currentTimeMillis()-t1) +" ms");

        long t2 = System.currentTimeMillis();
        List<Tuple3<Integer, long[], int[]>> concDiscStripesX = rddData.mapToPair(t->{
            grarchimedesai.archimedesai.distributed.grid.Grid gridBr = gridBroadcasted.getValue();
            return new Tuple2<>(gridBr.getXStripeId(t.getX()), t);
        }).groupByKey(partitioner).map(tup->{

            Grid gridBr = gridBroadcasted.getValue();

            int[] elementsPerCell = new int[(int)gridBr.getCellsInYAxis()];

            Iterator<Pair> it = tup._2.iterator();
            while (it.hasNext()) {
                Pair t = it.next();
                elementsPerCell[gridBr.getYStripeId(t.getY())]++;
            }

            Cell[] cells = new Cell[gridBr.getCellsInYAxis()];
            for (int i = 0; i < elementsPerCell.length; i++) {
                if(elementsPerCell[i]!=0){
                    cells[i] = Cell.newCell(elementsPerCell[i]);
                    elementsPerCell[i] = 0;
                }
            }

            it = tup._2.iterator();
            while (it.hasNext()) {
                Pair t = it.next();
                int stripeId = gridBr.getYStripeId(t.getY());
                cells[stripeId].getPairs()[elementsPerCell[stripeId]]=t;
                elementsPerCell[stripeId]++;
            }

            for (int i = 0; i < cells.length; i++) {
                if(cells[i]!=null){
                    Arrays.sort(cells[i].getPairs(), Comparator.comparing(Pair::getX));
                }
            }

            long[] numbers = new long[2];
            for (int i = elementsPerCell.length - 1; i > 0; i--) {
                if(elementsPerCell[i]!=0){
                    Pair[] current = cells[i].getPairs();
                    for (int j = i - 1; j >= 0; j--) {
                        if(elementsPerCell[j]!=0){
                            long[] numbersFromTwoCells = Algorithms.southTile(current, cells[j].getPairs());
                            numbers[0] = numbers[0] + numbersFromTwoCells[0];
                            numbers[1] = numbers[1] + numbersFromTwoCells[1];
                        }
                    }
                }
            }

            return new Tuple3<>(tup._1,numbers, elementsPerCell);
        }).collect();

        System.out.println("South tiles processing: " + (System.currentTimeMillis()-t2) + " ms");

        long t3 = System.currentTimeMillis();
        long lineCount = 0;
        int[][] histogram = new int[grid.getCellsInXAxis()][grid.getCellsInYAxis()];
        for (Tuple3<Integer, long[], int[]> discStripesX : concDiscStripesX) {
            concDiscStripesY[0] = concDiscStripesY[0] + discStripesX._2()[0];
            concDiscStripesY[1] = concDiscStripesY[1] + discStripesX._2()[1];
            int columnid = discStripesX._1();
            int[] rows = discStripesX._3();
            for (int i = 0; i < discStripesX._3().length; i++) {
                lineCount = lineCount + rows[i];
                histogram[columnid][i] = rows[i];
            }
        }
        concDiscStripesY[0] = concDiscStripesY[0] + Algorithms.discordantCells(histogram, grid.getCellsInXAxis(), grid.getCellsInYAxis());
        System.out.println("Histogram processing: " + (System.currentTimeMillis()-t3) + " ms");

        long concordants = ((lineCount*(lineCount-1))/2)-concDiscStripesY[0]-concDiscStripesY[1]-concDiscStripesY[2]-concDiscStripesY[3];
        double tau = (concordants-concDiscStripesY[0])/Math.sqrt((double)(concordants+concDiscStripesY[0]+concDiscStripesY[1])*(concordants+concDiscStripesY[0]+concDiscStripesY[2]));
        long elapsedtime = ((System.currentTimeMillis() - t1));

        System.out.println("tau is: " + tau);
        System.out.println("Elapsed time: "+elapsedtime+" ms");

        jsc.close();
        sparkSession.close();

    }




}