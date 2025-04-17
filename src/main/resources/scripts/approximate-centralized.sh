ookla-small: -0.3398997715054864
taxi-small: 0.6765504153316323
weather-small: -0.21136176048573735
radiation-small: -0.9799336217601233

uniform: -8.782000049626248E-5
diagonal: 0.9362368352778141
gaussian: -5.458098333292595E-5
sierpinski: 2.2501522839474145E-5

for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainApproximate data/ookla-small.csv 0 2 , 1.0 0.0 3932585.1 39598.1 "$i" "$i" timeExec-approx-gridAdaptive-ookla${j}.txt -0.3398997715054864
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainApproximate data/taxi-small.csv 0 2 , 0.0 0.0 6.40172993E8 861611.06 "$i" "$i" timeExec-approx-gridAdaptive-taxi${j}.txt 0.6765504153316323
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainApproximate data/weather-small.csv 0 2 , -115.0 0.0 141.1 100.1 "$i" "$i" timeExec-approx-gridAdaptive-weather${j}.txt -0.21136176048573735
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainApproximate data/radiation-small.csv 0 2 , 3.5 1.0 90.1 1351.1 "$i" "$i" timeExec-approx-gridAdaptive-radiation${j}.txt -0.9799336217601233
    done
done

for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainApproximate data/uniform.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-approx-gridAdaptive-uniform${j}.txt" -8.782000049626248E-5
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainApproximate data/diagonal.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-approx-gridAdaptive-diagonal${j}.txt" 0.9362368352778141
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainApproximate data/gaussian.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-approx-gridAdaptive-gaussian${j}.txt" -5.458098333292595E-5
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainApproximate data/sierpinski.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-approx-gridAdaptive-sierpinski${j}.txt" 2.2501522839474145E-5
    done
done






for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainAdaptiveApproximate data/ookla-small.csv 0 2 , 1.0 0.0 3932585.1 39598.1 "$i" "$i" timeExec-approx-gridAdaptive-ookla${j}.txt data/samples/ookla-small.csv -0.3398997715054864
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainAdaptiveApproximate data/taxi-small.csv 0 2 , 0.0 0.0 6.40172993E8 861611.06 "$i" "$i" timeExec-approx-gridAdaptive-taxi${j}.txt data/samples/taxi-small.csv 0.6765504153316323
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainAdaptiveApproximate data/weather-small.csv 0 2 , -115.0 0.0 141.1 100.1 "$i" "$i" timeExec-approx-gridAdaptive-weather${j}.txt data/samples/weather-small.csv -0.21136176048573735
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainAdaptiveApproximate data/radiation-small.csv 0 2 , 3.5 1.0 90.1 1351.1 "$i" "$i" timeExec-approx-gridAdaptive-radiation${j}.txt data/samples/radiation-small.csv -0.9799336217601233
    done
done

for j in $(seq 1 1 5); do
    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainAdaptiveApproximate data/uniform.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-approx-gridAdaptive-uniform${j}.txt" data/samples/uniform.csv -8.782000049626248E-5
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainAdaptiveApproximate data/diagonal.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-approx-gridAdaptive-diagonal${j}.txt" data/samples/diagonal.csv 0.9362368352778141
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainAdaptiveApproximate data/gaussian.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-approx-gridAdaptive-gaussian${j}.txt" data/samples/gaussian.csv -5.458098333292595E-5
    done

    for i in 5 10 25 50 100 200 400 800 1600 3200; do
        java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainAdaptiveApproximate data/sierpinski.csv 0 1 , 0 0 1 1 "$i" "$i" "timeExec-approx-gridAdaptive-sierpinski${j}.txt" data/samples/sierpinski.csv 2.2501522839474145E-5
    done
done