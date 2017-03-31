# LoRa REST API server with Elasticsearch and Kibana

## Installation on Ubuntu Server 16.04 LTS
### You can either follow the step-by-step tutorial or run the [startscript](#startscript) to setup and run the REST API.

##### 1. Step-by-step installation

* Go to the directory where you have clone the source code in this [step](../INSTALL.md#step2).

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
	
* Build source code

	```shell
	cd demos/msf4j-elasticsearch-kibana/proximus-enco/src/msf4j/
	mvn package
	```
	
* Run REST server

	```shell
	java -jar -Dspring.profiles.active=elasticsearch ./target/msf4j-0.1-SNAPSHOT.jar
	```
	
##### <a name="startscript"></a> 2. Installation using startscirpt

* Go to the directory where you have clone the source code in this [step](../INSTALL.md#step2).

* Install

	```shell
	cd loRa/demos/lora-rest-service/proximus-enco/src/install/elastic
	chmod +x rest-api-elastic.sh
	./rest-api-elastic.sh --install
	```
* Run REST server

	```shell
	./rest-api-elastic.sh --start
	```

##### 3. Configure Kibana dashboard

* Access Kibana server at http://localhost:5601
* Go to Management > Saved Objects.
* Click Import and choose [kibanaExport.json](kibana/kibana-export.json) under directory sources.
* To view the imported dashboard, go to Dashboard > open > MyDashboard


##### To stop the servers

```shell
./rest-api-elastic.sh --stop
```