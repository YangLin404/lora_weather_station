# Installing LoRa micro service
Follow steps below to get LoRa micro service running.
## <a name="Prerequirements"></a> Requirements
* Git
* JDK 1.8
* Maven

### <a name="step1">Step 1: Clone the source code</a>

Clone the source code by using following commands:

```shell
cd ~
mkdir lora
cd lora
git clone -b loRa-rest-service --single-branch https://i8c.githost.io/wso2/loRa
cd loRa/demo/weather_station/common/src/msf4j/install/
```

### Step 2: install 

The next step is to setup the loRa micro service on your Server. You can either use the microservice script or follow the step-by-step instruction to install the microservice.

* ##### Using [microservice script](./microservice.sh)

	* Make microservice.sh executable

		```shell
		chmod +x ./microservice.sh
		```
	* Install microservice with elasticsearch and kibana

		```shell
		sudo ./microservice.sh --install elastic
		```
	* Install microservice with Postgresql (Note. Analyzing tool is currently not provided.)

		```shell
		sudo ./microservice.sh --install postgresql
		```
* ##### Follow step-by-step install instruction

	* For [elasticsearch and kibana](./elastic/README.md)
	* For [postgresql](./postgresql/README.md)

### Step 3. <a name="step3">Configure application.properties</a>

After succesfully install the microservice, you will find the config file application.properties under ./target/config/folder. It is neccessary to edit it before first time starting the microservice. 

Please read [Config file description](CONFIG.md) and edit where neccessary.

### Step 4. <a name="configureKibana">Configure Kibana dashboard</a>

* Access Kibana server at http://\<hostname\>:5601
* Create index named lora
* Go to Management > Saved Objects.
* Click Import and choose [kibanaExport.json](elastic/kibana-export.json) under directory ./elastic/
* To view the imported dashboard, go to Dashboard > open > MyDashboard

### Step 5: Run

```shell
sudo ./microservice.sh --start [OPTIONS]
```
OPTIONS:

| **Parameter** | **Description** |
|---|---|
| elastic | using elasticsearch as database and kibana as dashboard |
| postgresql| using postgresql as database |
| proximus | receiving lorapackets from proximus over http protocol |
| http| receiving lorapackets from the thingsnetwork over http protocol |
| mqtt| receiving lorapackets from the thingsnetwork over mqtt protocol |
| bg| run microservice in background |

Example: following command will start microservice in background with elasticsearch as database and receiving lorapacket from the thingsnetwork over http protocol.

```shell
sudo ./microservice.sh --start elastic http bg
```

### Step 4: stop 

```shell
sudo ./microservice.sh --stop
```