# Kendall's Tau Correlation

[![Java](https://img.shields.io/badge/Java-11%2B-blue.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-Build-green.svg)](https://maven.apache.org/)
[![Apache Spark](https://img.shields.io/badge/Spark-3.x-orange.svg)](https://spark.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](#)

This project provides a scalable implementation of **Kendall's Tau** correlation, supporting both **centralized** and **distributed** modes (via Apache Spark). 
It features multiple algorithms, including Knight’s, Regular Grid, Adaptive Grid, and Approximate Tau.

## 📁 Dataset Information
Four datasets are included under `src/main/resources/data/`:

**Synthetic**: gaussian.csv, sierpinski.csv

**Real-world**: gaia.csv, radiation.csv

Sample datasets required for the Adaptive Grid algorithm are located in `src/main/resources/data/samples/`.

## 🧪 Plug-and-Play Testing
Test classes are located in `./src/test/`, organized into:
`centralized/`
`distributed/`

These tests are **plug-and-play** — no parameter configuration is required. They are pre-configured to work directly with the included datasets.

### Prerequisites

* Java
* Maven
* (For distributed tests) Apache Spark

### Run Centralized Tests
```
mvn -Dtest="centralized.*Test" test
```

### Run Distributed Tests (requires Spark)
```
mvn -Dtest="distributed.*Test" test
```

## ⚙️ Build Instructions
To build the JAR and extract dependencies:
```
mvn clean
mvn -Dmaven.test.skip=true package
mvn dependency:copy-dependencies -DoutputDirectory=lib
```

##  🚀 Usage Instructions

Replace <...> with your specific input values. All commands assume the working directory contains the compiled JAR and the lib/ folder.
<br />


#### Knight's Algorithm (Centralized)
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray <relative/full path file of the data set> <xColumnIndex> <yColumnIndex> <delimiter> <full/relative path of file for logging>
```
**Example**:
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainArray data/radiation.csv 0 2 , timeExecLog-knight-radiation.txt
```
<br />


#### Regular Grid (Centralized)
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid <relative/full path file of the data set> <xColumnIndex> <yColumnIndex> <delimiter> <minX> <minY> <maxX> <maxY> <cellsInXAxis> <cellsInYAxis> <full/relative path of file for logging>
```
**Example**:
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainGrid data/radiation.csv 0 2 , 3.5 1.0 90.1 1351.1 200 200 timeExecLog-gridRegular-radiation.txt
```
<br />

#### Adaptive Grid (Centralized)
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid <relative/full path file of the data set> <xColumnIndex> <yColumnIndex> <delimiter> <minX> <minY> <maxX> <maxY> <cellsInXAxis> <cellsInYAxis> <full/relative path of file for logging> <full/relative path of data sample>
```
**Example**:
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.MainAdaptiveGrid data/radiation.csv 0 2 , 3.5 1.0 90.1 1351.1 200 200 timeExecLog-gridAdaptive-radiation.txt data/samples/radiation-small.csv
```
<br />

#### Approximate Kendall Tau (Centralized)
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainApproximate <relative/full path file of the data set> <xColumnIndex> <yColumnIndex> <delimiter> <minX> <minY> <maxX> <maxY> <cellsInXAxis> <cellsInYAxis> <full/relative path of file for logging> <value of the actual kendall tau score>
```
**Example**:
```
java -Xmx4608m -cp parallel-kendalls-tau-1.0-SNAPSHOT.jar:lib/* gr.archimedesai.centralized.approximate.MainApproximate data/radiation.csv 0 2 , 3.5 1.0 90.1 1351.1 200 200 timeExecLog-approx-gridAdaptive-radiation.txt -0.9896842271210693
```
<br />

#### Regular Grid (Distributed)
```
spark-submit --class gr.archimedesai.distributed.Main --master local[*] --deploy-mode client --executor-memory 1g --executor-cores 2 --driver-memory 1g --num-executors 1 --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar <local or hdfs relative/full path of the data set file> <xColumnIndex> <yColumnIndex> <delimiter> <minX> <minY> <maxX> <maxY> <cellsInXAxis> <cellsInYAxis> <full/relative path of file for logging> <number of spark partitions>
```
**Example**:
```
spark-submit --class gr.archimedesai.distributed.Main --master local[*] --deploy-mode client --executor-memory 1g --executor-cores 2 --driver-memory 1g --num-executors 1 --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node1:9000/user/user/kendall-tau/radiation.csv 0 2 , 3.5 1.0 90.1 1351.1 200 200 timeExecLog-gridRegular-distributed-radiation.txt 16
```
<br />

#### Adaptive Grid (Distributed)
```
spark-submit --class gr.archimedesai.distributed.MainAdaptive --master local[*] --deploy-mode client --executor-memory 1g --executor-cores 2 --driver-memory 1g --num-executors 1 --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar <local or hdfs relative/full path of the data set file> <xColumnIndex> <yColumnIndex> <delimiter> <minX> <minY> <maxX> <maxY> <cellsInXAxis> <cellsInYAxis> <full/relative path of file for logging> <number of spark partitions> <full/relative path of data sample>
```
**Example**:
```
spark-submit --class gr.archimedesai.distributed.MainAdaptive --master local[*] --deploy-mode client --executor-memory 1g --executor-cores 2 --driver-memory 1g --num-executors 1 --conf spark.network.timeout=600s parallel-kendalls-tau-1.0-SNAPSHOT.jar hdfs://node1:9000/user/user/kendall-tau/radiation.csv 0 2 , 3.5 1.0 90.1 1351.1 200 200 timeExecLog-gridAdaptive-distributed-radiation.txt 16 data/samples/radiation-small.csv
```

## 📜 License
This project is licensed under the MIT License.

