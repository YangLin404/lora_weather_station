# LoRa RestApi with Elasticsearch and Kibana

## Installation on Ubuntu Server 16.04 LTS
### You can either follow the step-by-step tutorial or run the [startscript](#startscript) to setup and run the RestApi.

##### 1. Step-by-step installation

* Go to the directory where you have clone the source code in this [step](../../README.md#step2).

* Download and unzip Elasticsearch  
		
    ```shell
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.2.2.zip
    unzip elasticsearch-5.2.2.zip
    
    ```
* Run Elasticsearch in background

	```shell
	./elasticsearch-5.2.2/bin/elasticsearch -d
	```
* Download and unzip Kibana

	```shell
	wget https://artifacts.elastic.co/downloads/kibana/kibana-5.2.2-linux-x86_64.tar.gz
	tar -xzf kibana-5.2.2-linux-x86_64.tar.gz
	```
* Run Kibana in background

	```shell
	./kibana-5.2.2-linux-x86_64/bin/kibana &
	disown
	```
	
* Build sourcecode

	```shell
	cd loRa/Demo\'s/msf4j_elasticsearch_kibana/services/msf4j/
	mvn package
	```
	
* Run RestApi

	```shell
	java -jar ./target/msf4j-0.1-SNAPSHOT-elastic.jar
	```
	
##### <a name="startscript"></a> 2. Installation using startscirpt

* Go to the directory where you have clone the source code in this [step](../../README.md#step2).

* Install

	```shell
	cd loRa/Demo\'s/msf4j_elasticsearch_kibana/proximusEnco
	chmod +x restApi.sh
	sudo ./restApi.sh --install
	```
* Run RestApi and its necessary components

	```shell
	./restApi.sh --start
	```

##### 3. Now you can access Kibana dashboard at http://localhost:6501