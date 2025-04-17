for i in $(seq 1 1 5); do
    java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/ookla-small.csv 0 2 , timeExec-knight-ookla.txt
done

for i in $(seq 1 1 5); do
    java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/taxi-small.csv 0 2 , timeExec-knight-taxi.txt
done

for i in $(seq 1 1 5); do
    java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/weather-small.csv 0 2 , timeExec-knight-weather.txt
done

for i in $(seq 1 1 5); do
    java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/radiation-small.csv 0 2 , timeExec-knight-radiation.txt
done



for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/ookla-small.csv 0 2 , 1.0 0.0 3932585.1 39598.1 "$i" "$i" timeExec-gridRegular-ookla${j}.txt
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/taxi-small.csv 0 2 , 0.0 0.0 6.40172993E8 861611.06 "$i" "$i" timeExec-gridRegular-taxi${j}.txt
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/weather-small.csv 0 2 , -115.0 0.0 141.1 100.1 "$i" "$i" timeExec-gridRegular-weather${j}.txt
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/radiation-small.csv 0 2 , 3.5 1.0 90.1 1351.1 "$i" "$i" timeExec-gridRegular-radiation${j}.txt
    done
done

for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/ookla-small.csv 0 2 , 1.0 0.0 3932585.1 39598.1 "$i" "$i" timeExec-gridAdaptive-ookla${j}.txt data/samples/ookla-small.csv
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/taxi-small.csv 0 2 , 0.0 0.0 6.40172993E8 861611.06 "$i" "$i" timeExec-gridAdaptive-taxi${j}.txt data/samples/taxi-small.csv
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/weather-small.csv 0 2 , -115.0 0.0 141.1 100.1 "$i" "$i" timeExec-gridAdaptive-weather${j}.txt data/samples/weather-small.csv
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/radiation-small.csv 0 2 , 3.5 1.0 90.1 1351.1 "$i" "$i" timeExec-gridAdaptive-radiation${j}.txt data/samples/radiation-small.csv
    done
done

