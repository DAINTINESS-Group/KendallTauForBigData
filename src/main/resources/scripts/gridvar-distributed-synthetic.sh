for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.Main --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/uniform.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridRegular-distributed-uniform${j}.txt 192
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.Main --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/diagonal.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridRegular-distributed-diagonal${j}.txt 192
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.Main --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/gaussian.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridRegular-distributed-gaussian${j}.txt 192
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.Main --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/sierpinski.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridRegular-distributed-sierpinski${j}.txt 192
    done
done


for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/uniform.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridAdaptive-distributed-uniform${j}.txt 192 data/samples/uniform.csv
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/diagonal.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridAdaptive-distributed-diagonal${j}.txt 192 data/samples/diagonal.csv
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/gaussian.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridAdaptive-distributed-gaussian${j}.txt 192 data/samples/gaussian.csv
    done

    for i in 5 10 25 50 100 200 400; do
        spark-submit --class gr.archimedesai.distributed.MainAdaptive --master yarn --deploy-mode client --conf spark.yarn.archive="hdfs://node13:9000/user/spark-jars.tar.gz" --executor-memory 4608m --executor-cores 2 --driver-memory 5g --num-executors 12 --conf spark.network.timeout=600s --conf "spark.executor.extraJavaOptions=-Xms4608m" --conf "spark.driver.extraJavaOptions=-Xms5g" parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node13:9000/user/user/kendall-tau/sierpinski.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridAdaptive-distributed-sierpinski${j}.txt 192 data/samples/sierpinski.csv
    done
done
