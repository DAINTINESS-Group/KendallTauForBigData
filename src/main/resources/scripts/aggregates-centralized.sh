for j in $(seq 1 1 5); do
    java -Xmx4608m -Xms4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGridAggregated data/weather-small.csv 0 2 , -115.0 0.0 141.1 100.1 50 50 timeExec-gridAdaptive-aggregated-weather.txt data/samples/weather-small.csv
done