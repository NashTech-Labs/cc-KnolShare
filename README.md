KnolShare - Platform to share Knowledge


### How to run processing module :

Pre-requisite :  Running Zookeeper and kafka

Below steps are required to run processing module

1) Start Zookeeper and Kafka
2) Clone this repository
3) cd cc-KnolShare
4) run commands: 
 ```
 1) activator
 2) project processing
 3) run (it will by default run on 9000 port on localhost, you can use a different port as well)
 4) hit the micro-service from your browser : 
 localhost:9000
 ```
###### Steps to Install and Run Zookeeper and Kafka on your system :

Step 1: Download Kafka

Download Kafka from [here](https://www.apache.org/dyn/closer.cgi?path=/kafka/0.10.1.1/kafka_2.11-0.10.1.1.tgz)

Step 2: Extract downloaded file

```tar -xzvf kafka_2.11-0.10.1.1.tgz
cd kafka_2.11-0.10.1.1
Step 3: Start Servers
```

Start Zookeeper:
```
bin/zookeeper-server-start.sh config/zookeeper.properties
```
Start Kafka server:
```
bin/kafka-server-start.sh config/server.properties
```