# Kendall's Tau Correlation


For the validation of the code you can check the test classes that exist in the project. 
These classes do not require any parameter setting (plug and play) and have already been set to operate on the data that exist in the path src/main/resources.
In total, four data sets have been uploaded, two synthetic (gaussian, sierpinski) and two real (taxi, radiation). A sample of them exist in the same directory under the folder samples, as they are required for the Adaptive Grid.

All tests exist under the directory ./src/test/ that consist of two packages, centralized and distributed folders.
To run the test classes it is required to have maven already installed.
Each class contains four methods, one for each data set.

To run all of the test classes that operate in centralized mode, execute:
```
mvn -Dtest="centralized.*Test" test
```
To run the test classes that operate in distributed mode (requires the setup of Apache Spark in your machine), execute:
```
mvn -Dtest="distributed.*Test" test
```

In case that you want to test the scripts on your data sets, at first you have  to build the jar file and extract the libraries. This can be done via this command:
```
mvn clean && mvn -Dmaven.test.skip=true package && mvn dependency:copy-dependencies -DoutputDirectory=lib
```
Then, you can use the following commands to execute the kendall's tau computation for the respective mode.

#### Knight's Algorithm (Centralized mode)
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* grarchimedesai.archimedesai.centralized.MainArray <relative/full path file of the data set> <xColumnIndex> <yColumnIndex> <delimiter> <full/relative path of file for logging>
```
Example:
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* grarchimedesai.archimedesai.centralized.MainArray data/radiation.csv 0 2 , timeExecLog-knight-radiation.txt
```
<br />


#### Regular Grid (Centralized mode)
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* grarchimedesai.archimedesai.centralized.MainGrid <relative/full path file of the data set> <xColumnIndex> <yColumnIndex> <delimiter> <minX> <minY> <maxX> <maxY> <cellsInXAxis> <cellsInYAxis> <full/relative path of file for logging>
```
Example:
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* grarchimedesai.archimedesai.centralized.MainGrid data/radiation.csv 0 2 , 3.5 1.0 90.1 1351.1 200 200 timeExecLog-gridRegular-radiation.txt
```
<br />

#### Adaptive Grid (Centralized mode)
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* grarchimedesai.archimedesai.centralized.MainAdaptiveGrid <relative/full path file of the data set> <xColumnIndex> <yColumnIndex> <delimiter> <minX> <minY> <maxX> <maxY> <cellsInXAxis> <cellsInYAxis> <full/relative path of file for logging> <full/relative path of data sample>
```
Example:
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* grarchimedesai.archimedesai.centralized.MainAdaptiveGrid data/radiation.csv 0 2 , 3.5 1.0 90.1 1351.1 200 200 timeExecLog-gridAdaptive-radiation.txt data/samples/radiation-small.csv
```
<br />

#### Approximate Kendall Tau (Centralized mode)
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* grarchimedesai.archimedesai.centralized.approximate.MainApproximate <relative/full path file of the data set> <xColumnIndex> <yColumnIndex> <delimiter> <minX> <minY> <maxX> <maxY> <cellsInXAxis> <cellsInYAxis> <full/relative path of file for logging> <value of the actual kendall tau score>
```
Example:
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* grarchimedesai.archimedesai.centralized.approximate.MainApproximate data/radiation.csv 0 2 , 3.5 1.0 90.1 1351.1 200 200 timeExecLog-approx-gridAdaptive-radiation.txt -0.9896842271210693
```
<br />

#### Regular Grid (Distributed mode)
```
spark-submit --class grarchimedesai.archimedesai.distributed.Main --master local[*] --deploy-mode client --executor-memory 1g --executor-cores 2 --driver-memory 1g --num-executors 1 --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar <local or hdfs relative/full path of the data set file> <xColumnIndex> <yColumnIndex> <delimiter> <minX> <minY> <maxX> <maxY> <cellsInXAxis> <cellsInYAxis> <full/relative path of file for logging> <number of spark partitions>
```
Example:
```
spark-submit --class grarchimedesai.archimedesai.distributed.Main --master local[*] --deploy-mode client --executor-memory 1g --executor-cores 2 --driver-memory 1g --num-executors 1 --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node1:9000/user/user/kendall-tau/radiation.csv 0 2 , 3.5 1.0 90.1 1351.1 200 200 timeExecLog-gridRegular-distributed-radiation.txt 16
```
<br />

#### Adaptive Grid (Distributed mode)
```
spark-submit --class grarchimedesai.archimedesai.distributed.MainAdaptive --master local[*] --deploy-mode client --executor-memory 1g --executor-cores 2 --driver-memory 1g --num-executors 1 --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar <local or hdfs relative/full path of the data set file> <xColumnIndex> <yColumnIndex> <delimiter> <minX> <minY> <maxX> <maxY> <cellsInXAxis> <cellsInYAxis> <full/relative path of file for logging> <number of spark partitions> <full/relative path of data sample>
```
Example:
```
spark-submit --class grarchimedesai.archimedesai.distributed.MainAdaptive --master local[*] --deploy-mode client --executor-memory 1g --executor-cores 2 --driver-memory 1g --num-executors 1 --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node1:9000/user/user/kendall-tau/radiation.csv 0 2 , 3.5 1.0 90.1 1351.1 200 200 timeExecLog-gridAdaptive-distributed-radiation.txt 16 data/samples/radiation-small.csv
```