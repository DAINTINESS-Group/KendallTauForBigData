for j in $(seq 1 1 5); do
    for i in 15 30 45 60; do
        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/weather-small-${i}.csv 0 2 , timeExec-knight-weather-small-${i}.txt
    done
done

#for j in $(seq 1 1 5); do
#    for i in 15 30 45 60; do
#        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/taxi-small-${i}.csv 0 2 , timeExec-knight-taxi-small-${i}.txt
#    done
#done

for j in $(seq 1 1 5); do
    for i in 15 30 45 60; do
        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/gaia-small-${i}.csv 0 2 , timeExec-knight-gaia-small-${i}.txt
    done
done



for j in $(seq 1 1 5); do
    for i in 15 30 45 60; do
        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/weather-small-${i}.csv 0 2 , -115.0 0.0 141.1 100.1 25 25 "timeExec-gridRegular-weather-small-${i}-${j}.txt"
    done

#    for i in 15 30 45 60; do
#        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/taxi-small-${i}.csv 0 2 , 0.0 0.0 6.40172993E8 861611.06 25 25 "timeExec-gridRegular-taxi-small-${i}-${j}.txt"
#    done

    for i in 15 30 45 60; do
        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/gaia-small-${i}.csv 0 2 , 2.32451 2.0164533 22.58641 24.695998 25 25 "timeExec-gridRegular-gaia-small-${i}-${j}.txt"
    done
done



for j in $(seq 1 1 5); do
    for i in 15 30 45 60; do
        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/weather-small-${i}.csv 0 2 , -115.0 0.0 141.1 100.1 25 25 timeExec-gridAdaptive-weather-small-${i}-${j}.txt data/samples/weather-small-${i}.csv
    done

#    for i in 15 30 45 60; do
#        java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/taxi-small-${i}.csv 0 2 , 0.0 0.0 6.40172993E8 861611.06 25 25 timeExec-gridAdaptive-taxi-small-${i}-${j}.txt data/samples/taxi-small-${i}.csv
#    done

     for i in 15 30 45 60; do
         java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/gaia-small-${i}.csv 0 2 , 2.32451 2.0164533 22.58641 24.695998 25 25 timeExec-gridAdaptive-gaia-small-${i}-${j}.txt data/samples/gaia-small-${i}.csv
     done
done



