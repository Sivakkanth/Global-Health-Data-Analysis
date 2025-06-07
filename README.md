# Global Health Statistics Analysis

## Objective
This project performs large scale data analysis on a global health dataset using the MapReduce programming model. The objective is to discover meaningful patterns by executing five analytical tasks, each implemented through a custom reducer. 

## Team Members
- EG/2020/3896 – Dilexsan H.A.L.
- EG/2020/4220 – Sivakkanth K.
- EG/2020/4366 – Nifras M.J.M.

## Prerequisites
Ensure the following software is installed on your system:

- **Java Development Kit (JDK)** – Version 8 or above  
- **Apache Hadoop** – Version 3.0+  
- **Apache Maven** – For dependency management  
- **IDE** – IntelliJ IDEA / Eclipse for Java development

## Dataset Link
https://www.kaggle.com/datasets/malaiarasugraj/global-health-statistics

## Setup Instructions

### 1. Install Apache Hadoop - Windows
Follow the steps below or refer to the [Hadoop Official Setup Guide](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html):

```bash
# Download Hadoop
# Visit: https://hadoop.apache.org/releases.html and download a stable version

# Follow the instructions to install hadoop
https://medium.com/@riturajrudra18/how-to-install-hadoop-in-windows-10-and-11-9a306814ccf0

# Running hadoop
# Format Namenode - only the first time
hdfs namenode -format

# Start Hadoop daemons
start-dfs.cmd
start-yarn.cmd

# checking whether correctly install namenode and datanode command
jps

```
## 2 Setup project

```bash
# Clone or download the project repository
git clone https://github.com/Sivakkanth/Global-Health-Data-Analysis.git
cd global-health-mapreduce

# Build the project using Maven
mvn clean package
```

