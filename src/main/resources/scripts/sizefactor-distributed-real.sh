for j in $(seq 1 1 5); do
    for i in 1 2 3; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/weather-f${i}.csv 0 2 , -116.0 0.0 143.1 100.1 200 200 timeExec-gridAdaptive-distributed-weather-f${i}.txt 192 data/samples/weather-f${i}.csv
    done
done
#for j in $(seq 1 1 5); do
#    for i in 1 2 3 4; do
#        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/taxi-f${i}.csv 0 2 , 0.0 0.0 7.5223897E9 1.0000017E7 200 200 timeExec-gridAdaptive-distributed-taxi-f${i}.txt 192 data/samples/taxi-f${i}.csv
#    done
#done

for j in $(seq 1 1 5); do
    for i in 1 2 3 4; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/gaia-f${i}.csv 0 2 , 1.731607 1.7436333 22.92213 24.695998 200 200 timeExec-gridAdaptive-distributed-gaia-f${i}.txt 192 data/samples/gaia-f${i}.csv
    done
done

Min: 1.731607 2.3980012 1.7436333
Max: 22.92212 25.32663 24.695997