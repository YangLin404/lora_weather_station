# LoRa micro service with Elasticsearch and Kibana

## Installation on Ubuntu Server 16.04 LTS

### 1. Install elasticsearch and kibana

* Go to the directory where you have clone the source code in this [step](../README.md#step1).

* Download and unzip Elasticsearch 5.2.2
		
    ```shell
    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.2.2.zip -P ~/lora
    unzip ~/lora/elasticsearch-5.2.2.zip -d ~/lora
    
    ```
* Download and unzip Kibana 5.2.2

	```shell
	wget https://artifacts.elastic.co/downloads/kibana/kibana-5.2.2-linux-x86_64.tar.gz -P ~/lora
	sudo tar -xzf ~/lora/kibana-5.2.2-linux-x86_64.tar.gz -C ~/lora
	```
	
* Configure Kibana

	```shell
	sed -i 's/#server.host: "localhost"/server.host: "0.0.0.0"/' ./kibana-5.2.2-linux-x86_64/config/kibana.yml;
	```
	
### 2. Build source code

	```shell
	cd loRa/demo/weather_station/common/src/msf4j/
	mvn package
	```
	
### 3. For further instructions please read [this.](../README.md#step3)