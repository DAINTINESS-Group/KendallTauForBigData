Min: 3.5 6.9 1.0 -9900.0 -9900.0 -9900.0
Max: 90.0 353.1 1351.0 0.568 0.575 0.7

3.5 1.0 90.1 1351.1

java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainAdaptiveApproximate data/radiation-small.csv 0 2 , 3.5 1.0 90.1 1351.1 50 50 timeExec-approx-gridAdaptive-radiation${j}.txt data/samples/radiation-small.csv -0.9799336217601233


for i in 5 10 25 50 100 200 400 800 1600 3200; do
    java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainAdaptiveApproximate data/radiation-small.csv 0 2 , 3.5 1.0 90.1 1351.1 "$i" "$i" timeExec-approx-gridAdaptive-radiation${j}.txt data/samples/radiation-small.csv -0.9799336217601233
done
