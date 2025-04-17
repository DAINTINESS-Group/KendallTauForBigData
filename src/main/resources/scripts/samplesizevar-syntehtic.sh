for j in $(seq 1 1 5); do
    for i in 1 2 3; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/sierpinski.csv 0 1 , 0 0 1 1 200 200 timeExec-gridAdaptive-distributed-sierpinski-sample-${i}.txt 192 data/samples/sierpinski-sample-${i}.csv
    done

    for i in 1 2 3; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/gaussian.csv 0 1 , 0 0 1 1 200 200 timeExec-gridAdaptive-distributed-gaussian-sample-${i}.txt 192 data/samples/gaussian-sample-${i}.csv
    done
done


for j in $(seq 1 1 5); do
    for i in 1 2 3; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/sierpinski.csv 0 1 , 0 0 1 1 50 50 timeExec-gridAdaptive-sierpinski-sample-${i}.txt data/samples/sierpinski-sample-${i}.csv
    done

    for i in 1 2 3; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/gaussian.csv 0 1 , 0 0 1 1 50 50 timeExec-gridAdaptive-gaussian-sample-${i}.txt data/samples/gaussian-sample-${i}.csv
    done
done
