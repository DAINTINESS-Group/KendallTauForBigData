for j in $(seq 1 1 3); do
    for i in 3 6 9 12; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors "$i" --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/taxi-f1.csv 0 2 , 0.0 0.0 7.5223897E9 1.0000017E7 200 200 timeExec-gridAdaptive-distributed-executors${i}-taxi-f1.txt 192 data/samples/taxi-f1.csv
    done
done

for j in $(seq 1 1 3); do
    for i in 3 6 9 12; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors "$i" --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/weather-f1.csv 0 2 , -116.0 0.0 143.1 100.1 200 200 timeExec-gridAdaptive-distributed-executors${i}-weather-f1.txt 192 data/samples/weather-f1.csv
    done
done

for j in $(seq 1 1 3); do
    for i in 3 6 9 12; do
        spark-submit --class gr.archimedesai.distributed.Main --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors "$i" --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/weather-f1.csv 0 2 , -116.0 0.0 143.1 100.1 200 200 timeExec-gridRegular-distributed-executors${i}-weather-f1.txt 192
    done
done
