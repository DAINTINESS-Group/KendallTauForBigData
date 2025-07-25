for j in $(seq 1 1 5); do
    for i in 1 2 3 4; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/sierpinski-f${i}.csv 0 1 , 0 0 1 1 200 200 timeExec-gridAdaptive-distributed-sierpinski-f${i}.txt 192 data/samples/sierpinski-f${i}.csv
    done
done

for j in $(seq 1 1 5); do
    for i in 1 2 3 4; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/gaussian-f${i}.csv 0 1 , 0 0 1 1 200 200 timeExec-gridAdaptive-distributed-gaussian-f${i}.txt 192 data/samples/gaussian-f${i}.csv
    done
done

