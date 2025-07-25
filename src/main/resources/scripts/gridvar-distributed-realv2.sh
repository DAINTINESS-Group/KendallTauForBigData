for j in $(seq 1 1 5); do
#    for i in 5 10 25 50 100 200 400; do
#        spark-submit --class gr.archimedesai.distributed.Main --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/ookla-small.csv 0 2 , 1.0 0.0 3932585.1 39598.1 "$i" "$i" timeExec-gridRegular-distributed-ookla${j}.txt 192
#    done
#
#    for i in 5 10 25 50 100 200 400; do
#        spark-submit --class gr.archimedesai.distributed.Main --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/taxi-small.csv 0 2 , 0.0 0.0 6.40172993E8 861611.06 "$i" "$i" timeExec-gridRegular-distributed-taxi${j}.txt 192
#    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.Main --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/weather-small.csv 0 2 , -115.0 0.0 141.1 100.1 "$i" "$i" timeExec-gridRegular-distributed-weather${j}.txt 192
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.Main --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/radiation-small.csv 0 2 , 3.5 1.0 90.1 1351.1 "$i" "$i" timeExec-gridRegular-distributed-radiation${j}.txt 192
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.Main --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/gaia-small.csv 0 2 , 2.32451 2.0164533 22.697629 24.695998 "$i" "$i" timeExec-gridRegular-distributed-gaia${j}.txt 192
    done
done

for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/ookla-small.csv 0 2 , 1.0 0.0 3932585.1 39598.1 "$i" "$i" timeExec-gridAdaptive-distributed-ookla${j}.txt 192 data/samples/ookla-small.csv
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/taxi-small.csv 0 2 , 0.0 0.0 6.40172993E8 861611.06 "$i" "$i" timeExec-gridAdaptive-distributed-taxi${j}.txt 192 data/samples/taxi-small.csv
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/weather-small.csv 0 2 , -115.0 0.0 141.1 100.1 "$i" "$i" timeExec-gridAdaptive-distributed-weather${j}.txt 192 data/samples/weather-small.csv
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/radiation-small.csv 0 2 , 3.5 1.0 90.1 1351.1 "$i" "$i" timeExec-gridAdaptive-distributed-radiation${j}.txt 192 data/samples/radiation-small.csv
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/gaia-small.csv 0 2 , 2.32451 2.0164533 22.697629 24.695998 "$i" "$i" timeExec-gridAdaptive-distributed-gaia${j}.txt 192 data/samples/gaia-small.csv
    done
done
