# LoRa service with Elasticsearch and Kibana

## Installation on Ubuntu Server 16.04 LTS
 You can either follow the step-by-step tutorial or run the [microservice script](#startscript) to setup and run the micro service.

### 1. Step-by-step installation

* Go to the directory where you have clone the source code in this [step](../INSTALL.md#step1).

* Download and unzip Elasticsearch 5.2.2
		
    ```shell
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.2.2.zip
    unzip elasticsearch-5.2.2.zip
    
    ```
* Run Elasticsearch in background

	```shell
	./elasticsearch-5.2.2/bin/elasticsearch -d
	```
* Download and unzip Kibana 5.2.2

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
	cd loRa/demo/weather_station/ttn/mqtt/src/msf4j/
	mvn package
	```
	
* Run micro service with elasticsearch as database and http as protocol

	```shell
	java -jar -Dspring.profiles.active=elasticsearch,http ./target/msf4j-0.1-SNAPSHOT.jar
	```
	
* Run micro service with elasticsearch as database and mqtt as protocol

	```shell
	java -jar -Dspring.profiles.active=elasticsearch,mqtt ./target/msf4j-0.1-SNAPSHOT.jar
	```
	
### <a name="startscript">2. Installation using microservice script</a>

* Go to the directory where you have clone the source code in this [step](../INSTALL.md#step2).

* Install

	```shell
	cd loRa/demos/weather_station/ttn/mqtt/src/msf4j/install/elastic
	chmod +x microservice.sh
	./microservice.sh --install
	```
* [Configure application.properties](#config)

* Run micro service with elasticsearch as database

	```shell
	./microservice.sh --start
	```
	
### <a name="config">3. Configure application.properties</a>

After succesfully build the source code, you will find the config file application.properties under ./target/config/folder. It is neccessary to edit it before first time starting the microservice. 

Please read [Config file description](../CONFIG.md) and edit where neccessary.

### 4. Configure Kibana dashboard

* Access Kibana server at http://localhost:5601
* Go to Management > Saved Objects.
* Click Import and choose [kibanaExport.json](../kibana/kibana-export.json) under directory sources.
* To view the imported dashboard, go to Dashboard > open > MyDashboard


### 5. Regardless of used installation method, the microservice script is able to perform following action.

* To start the micro service with mqtt protocol and elasticsearch. 

	```shell
	./microservice-elastic.sh --start mqtt
	```
* To stop the micro service with http protocol and elasticsearch.

	```shell
	./microservice-elastic.sh --stop http
	```	