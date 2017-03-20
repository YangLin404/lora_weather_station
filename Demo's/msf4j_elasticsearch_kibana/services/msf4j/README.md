# LoRa RestApi with Elasticsearch and Kibana

## Installation on Ubuntu Server 16.04 LTS
### You can either follow the step-by-step tutorial or run the [startscript](#startscript) to setup and run the RestApi.

##### > step-by-step installation

1. Prepare  

 	```shell
   cd ~
   mkdir rest-api
   cd rest-api
   ```
2. Download and unzip Elasticsearch  
		
    ```shell
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.2.2.zip
    unzip elasticsearch-5.2.2.zip
    
    ```
3. Run Elasticsearch in background

	```shell
	./elasticsearch-5.2.2/bin/elasticsearch -d
	```
4. Download and unzip Kibana

	```shell
	wget https://artifacts.elastic.co/downloads/kibana/kibana-5.2.2-linux-x86_64.tar.gz
	tar -xzf kibana-5.2.2-linux-x86_64.tar.gz
	```
5. Run Kibana in background

	```shell
	./kibana-5.2.2-linux-x86_64/bin/kibana &
	disown
	```
    
6. Download RestApi's sourcecode

	```shell
	git clone -b loRa-RestService-Elastic-Kibana --single-branch https://i8c.githost.io/wso2/loRa
	```
7. Build sourcecode

	```shell
	cd loRa/Demo\'s/msf4j_elasticsearch_kibana/services/msf4j/
	mvn package
	```
	
8. Run RestApi

	```shell
	java -jar ./target/msf4j-0.1-SNAPSHOT-elastic.jar
	```
	
##### <a name="startscript"></a>> installation using startscirpt

1. Prepare

	```shell
   cd ~
   mkdir rest-api
   cd rest-api
   ```
2. Download RestApi's sourcecode

	```shell
	git clone -b loRa-RestService-Elastic-Kibana --single-branch
	https://i8c.githost.io/wso2/loRa
	```
3. Install

	```shell
	cd loRa/Demo\'s/msf4j_elasticsearch_kibana
	chmod +x restApi.sh
	sudo ./restApi.sh --install
	```
4. Run RestApi and its necessary components

	```shell
	./restApi.sh --start
	```
	