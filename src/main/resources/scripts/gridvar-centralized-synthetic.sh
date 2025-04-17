for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/uniform.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-gridRegular-uniform${j}.txt"
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/diagonal.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-gridRegular-diagonal${j}.txt"
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/gaussian.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-gridRegular-gaussian${j}.txt"
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/sierpinski.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-gridRegular-sierpinski${j}.txt"
    done
done

for i in $(seq 1 1 5); do
    java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/uniform.csv 0 1 , timeExec-knight-uniform.txt
done

for i in $(seq 1 1 5); do
    java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/diagonal.csv 0 1 , timeExec-knight-diagonal.txt
done

for i in $(seq 1 1 5); do
    java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/gaussian.csv 0 1 , timeExec-knight-gaussian.txt
done

for i in $(seq 1 1 5); do
    java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/sierpinski.csv 0 1 , timeExec-knight-sierpinski.txt
done

for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/uniform.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridAdaptive-uniform${j}.txt data/samples/uniform.csv
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/diagonal.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridAdaptive-diagonal${j}.txt data/samples/diagonal.csv
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/gaussian.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridAdaptive-gaussian${j}.txt data/samples/gaussian.csv
    done

    for i in 5 10 25 50 100 200 400; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/sierpinski.csv 0 1 , 0 0 1 1 "$i" "$i" timeExec-gridAdaptive-sierpinski${j}.txt data/samples/sierpinski.csv
    done
done
