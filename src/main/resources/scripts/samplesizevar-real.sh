for j in $(seq 1 1 5); do
#    for i in 1 2 3; do
#        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/taxi-small.csv 0 2 , 0.0 0.0 6.40172993E8 861611.06 200 200 timeExec-gridAdaptive-distributed-taxi-sample-${i}.txt 192 data/samples/taxi-small-sample-${i}.csv
#    done

    for i in 1 2 3; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/weather-small.csv 0 2 , -115.0 0.0 141.1 100.1 200 200 timeExec-gridAdaptive-distributed-weather-sample-${i}.txt 192 data/samples/weather-small-sample-${i}.csv
    done

    for i in 1 2 3; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/gaia-small.csv 0 2 , 2.32451 2.0164533 22.697629 24.695998 200 200 timeExec-gridAdaptive-distributed-gaia-sample-${i}.txt 192 data/samples/gaia-small-sample-${i}.csv
    done
done


for j in $(seq 1 1 5); do
#    for i in 1 2 3; do
#        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/taxi-small.csv 0 2 , 0.0 0.0 6.40172993E8 861611.06 50 50 timeExec-gridAdaptive-taxi-sample-${i}.txt data/samples/taxi-small-sample-${i}.csv
#    done

    for i in 1 2 3; do
        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/weather-small.csv 0 2 , -115.0 0.0 141.1 100.1 50 50 timeExec-gridAdaptive-weather-sample-${i}.txt data/samples/weather-small-sample-${i}.csv
    done

    for i in 1 2 3; do
        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/gaia-small.csv 0 2 , 2.32451 2.0164533 22.697629 24.695998 50 50 timeExec-gridAdaptive-gaia-sample-${i}.txt data/samples/gaia-small-sample-${i}.csv
    done
done
