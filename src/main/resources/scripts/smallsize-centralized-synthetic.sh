for j in $(seq 1 1 5); do
    for i in 15 30 45 60; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/gaussian-${i}.csv 0 1 , timeExec-knight-gaussian-${i}.txt
    done

    for i in 15 30 45 60; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/sierpinski-${i}.csv 0 1 , timeExec-knight-sierpinski-${i}.txt
    done
done

for j in $(seq 1 1 5); do
    for i in 15 30 45 60; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/gaussian-${i}.csv 0 1 , 0 0 1 1 50 50 "timeExec-gridRegular-gaussian-${i}-${j}.txt"
    done

    for i in 15 30 45 60; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/sierpinski-${i}.csv 0 1 , 0 0 1 1 50 50 "timeExec-gridRegular-sierpinski-${i}-${j}.txt"
    done
done

for j in $(seq 1 1 5); do
    for i in 15 30 45 60; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/gaussian-${i}.csv 0 1 , 0 0 1 1 50 50 timeExec-gridAdaptive-gaussian-${i}-${j}.txt data/samples/gaussian-${i}.csv
    done

    for i in 15 30 45 60; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/sierpinski-${i}.csv 0 1 , 0 0 1 1 50 50 timeExec-gridAdaptive-sierpinski-${i}-${j}.txt data/samples/sierpinski-${i}.csv
    done
done
