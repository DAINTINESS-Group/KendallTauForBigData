package gr.archimedesai.distributed;

import gr.archimedesai.Pair;
import gr.archimedesai.distributed.grid.AdaptiveGrid;
import gr.archimedesai.shapes.Point;
import gr.archimedesai.shapes.Rectangle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.*;

public class MergeSort {
    public static void main(String[] args) {
        SparkConf sparkConf = null;
        try {
            sparkConf = new SparkConf().set("spark.serializer", "org.apache.spark.serializer.KryoSerializer").set("spark.kryo.registrationRequired", "false")
                    .registerKryoClasses(new Class[]{ArrayList.class,Object.class, AdaptiveGrid.class, HashMap.class, Point.class, Rectangle.class, Pair.class, java.lang.invoke.SerializedLambda.class, org.apache.spark.util.collection.CompactBuffer[].class, org.apache.spark.util.collection.CompactBuffer.class, Class.forName("scala.reflect.ManifestFactory$ObjectManifest")/*,,scala.reflect.ManifestFactory.class*/});
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        SparkSession sparkSession = SparkSession.builder().config(sparkConf)/*.master("local[2]")*/.getOrCreate();
        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(sparkSession.sparkContext());
        long t1 = System.currentTimeMillis();

        final int xIndex = Integer.parseInt(args[1]);
        final int yIndex = Integer.parseInt(args[2]);

        // Step 1: Input data (distributed)
//        List<Integer> data = Arrays.asList(5, 3, 8, 1, 9, 2, 7, 6, 4);
//        JavaRDD<Integer> input = jsc.parallelize(data, 4); // 4 partitions

        JavaRDD<String> rddString = jsc.textFile(args[0]);
        JavaRDD<Integer> input = rddString.map((String line) -> {
            String[] elements = line.split(args[3]);
//            double x = Double.parseDouble(elements[xIndex]);
//            double y = Double.parseDouble(elements[yIndex]);
//            return Pair.newPair(x,y);
            return Integer.parseInt(elements[xIndex]);
        }).persist(StorageLevel.MEMORY_ONLY());

        // Step 2: Locally sort within each partition
        JavaPairRDD<Integer, List<Integer>> locallySorted = input.mapPartitionsWithIndex(
                new Function2<Integer, Iterator<Integer>, Iterator<Tuple2<Integer, List<Integer>>>>() {
                    @Override
                    public Iterator<Tuple2<Integer, List<Integer>>> call(Integer index, Iterator<Integer> iter) {
                        List<Integer> list = new ArrayList<>();
                        iter.forEachRemaining(list::add);
                        Collections.sort(list);
                        return Collections.singletonList(new Tuple2<>(index, list)).iterator();
                    }
                }, true).mapToPair(i->i);


        // Step 3: Iteratively merge partitions
        JavaPairRDD<Integer, List<Integer>> globallySorted = mergeSortedPartitions(locallySorted);

        // Step 4: Output
        List<Tuple2<Integer, List<Integer>>> result = globallySorted.collect();
        System.out.println("Globally Sorted Output:");
//        result.forEach(o->System.out.println(o._2));

        long elapsedtime = ((System.currentTimeMillis() - t1) / 1000);
        System.out.println(elapsedtime);

        jsc.stop();
        sparkSession.close();
    }

    public static JavaPairRDD<Integer, List<Integer>> mergeSortedPartitions(JavaPairRDD<Integer, List<Integer>> locallySorted) {
        JavaPairRDD<Integer, List<Integer>> currentRDD = locallySorted;
        int numPartitions = currentRDD.getNumPartitions();

        while (numPartitions > 1) {
            System.out.println(numPartitions);
            final int partitions = numPartitions;

            currentRDD = currentRDD.mapToPair(i-> {
                return new Tuple2<>(i._1/2,i._2);
            });

            JavaPairRDD<Integer, List<Integer>> reduced = currentRDD.reduceByKey(
                    new Function2<List<Integer>, List<Integer>, List<Integer>>() {
                        @Override
                        public List<Integer> call(List<Integer> a, List<Integer> b) {
                            return mergeLists(a, b);
                        }
                    });

            // Repartition to half
            currentRDD = reduced.repartition(numPartitions / 2);
            numPartitions = currentRDD.getNumPartitions();
        }
        return currentRDD;
    }

    // Classic two-way merge for sorted lists
    public static List<Integer> mergeLists(List<Integer> a, List<Integer> b) {
        List<Integer> result = new ArrayList<>(a.size() + b.size());
        int i = 0, j = 0;

        while (i < a.size() && j < b.size()) {
            if (a.get(i) <= b.get(j)) {
                result.add(a.get(i++));
            } else {
                result.add(b.get(j++));
            }
        }

        while (i < a.size()) result.add(a.get(i++));
        while (j < b.size()) result.add(b.get(j++));
        return result;
    }
}
