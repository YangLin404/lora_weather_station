# LoRa REST API server with Proximus Enco

## Introduction

For this project, 2 [LORAWAN Rapid Development Kits](http://www.allthingstalk.com/lorawan-rapid-development-kit) are available from www.allthingstalk.com.  

These kits come with access to the following websites:

* https://maker.allthingstalk.com  
* https://devs.enco.io/dashboard  
username = kristof.lievens@i8c.be  
password = L0Ra2017!   


## High-level architecture
![architect](doc/img/lora-proximus-enco.jpg)
## Functional Specs

The purpose of this project is to build a system which analyzes the environmental quality of I8C office. The temperature, air quality, loudness level and humidity inside I8c office will be measured, stored, analyzed and visualized.

## Technical Specs
### Architecture
This solution contains the following components:  

* a sketch application which allows loRa device to send data over Proximus Lora network.
* a REST micro service, based on WSO2 MSF4J and Spring for receiving and progressing data's.
* a stand-alone [Elasticsearch](https://www.elastic.co) server for storing data's.
* a stand-alone [Kibana](https://www.elastic.co/products/kibana) server for analyzing and visualizing data's.

### <a name="Prerequirements"></a> Prerequirements
* Git
* JDK
* Maven
* [LORAWAN Rapid Development Kits](http://www.allthingstalk.com/lorawan-rapid-development-kit)
* [Arduino IDE](https://www.arduino.cc/en/main/software)

### Dependencies
All required dependencies are included in source code or will be downloaded during the installation process.

1. ##### REST API micro service
	* MSF4J(will be downloaded)
	* Elasticsearch Java API(will be downloaded)

2. ##### sketch application
	* Arduino Sodaq Mbili Library(included)
	* [Adafruit Unified Sensor Driver](https://github.com/adafruit/Adafruit_Sensor)(included)
	* [Arduino Library for BME280 sensors](https://github.com/adafruit/Adafruit_BME280_Library)(included)
	
## Installation

Make sure you have installed all [prerequirements](#Prerequirements) listed above.

#### Step 1: Setup loRa device

As first step, follow these tutorials to getting started with loRa device.

* [Setup loRa device](http://support.sodaq.com/mbili/)
* [Getting started with Enco](http://docs.enco.io/docs/getting-started-with-enco)

#### <a name="step2"></a> Step 2: Clone the source code

Once you have activated en setup your loRa device, you can clone the source code by using following commands:

```shell
cd ~
mkdir lora-proximus
cd lora-proximus
git clone -b loRa-rest-service --single-branch https://i8c.githost.io/wso2/loRa
```

#### Step 3: Open sketch application

Now open the sketch application [send-data](src/arduino/send-data/send-data.ino) under src/arduino/send-data with Arduino IDE.

#### Step 4: Upload and run sketch application

Upload the sketch application by click ![upload logo](doc/img/arduino-upload.png) at top left corner of Arduino IDE. You can use the built-in serial monitor to check whether the application is running correctly or not.

#### Step 5: Create CloudChannel

Once the sketch application is running correctly on your LoRa device, you should follow the guide below to create CloudChannels API's which forward the data's to the REST API:

1. Go to [EnCo DevPortal](http://devs.enco.io/dashboard/) and log in with your Enco account.
2. Navigate to CloudChannels API. You should get a overview of your CloudChannels API's.
3. Click on the button ![New CloudChannel](doc/img/new-cloudchannel.png) to create a new CloudChannel. You should see something like below:
	![Create CloudChannel](doc/img/create-cloudchannel.png)

4. Here you can define where the data come from and where the data should go. For this demo we use LoRa as the input and HTTP as the output.
5. After drag & drop required components to input and output, you need to configure LoRa inbound configuration, CloudChannel definition and HTTP outbound configuration.
6. Configure the input: 
	* Click on ![LoRa](doc/img/lora-inbound.png)
	* Select your device.
7. Configure CloudChannel definition: 
	* Click on Edit definition
	* Give CloudChannel a relevant name
	* Select TemperatureSensor
	* Click Ok
8. Configure the output:
	* Click on ![http](doc/img/http.png)
	* Give a relevant Name
	* Endpoint: http://localhost:8287/service
	* HTTP method: POST
	* Click on Ok
9. Click Save Changes.
10. Redo above steps to create CloudChannels for AirQualitySensor, LoudnessSensor and HumiditySensor.

#### Step 6: Setup back-end

The next step is to setup the back-end on your Server, the instruction can be found [here](src/msf4j/README.md).

#### Step 7: Configure Kibana dashboard

Now it is time to configure the Kibana dashboard.

1. Access Kibana server at http://localhost:5601
2. Go to Management > Saved Objects.
3. Click Import and choose [kibanaExport.json](src/install/kibana/kibana-export.json) under directory sources.
4. To view the imported dashboard, go to Dashboard > open > MyDashboard

#### Step 8

##### All done! Now just wait for your loRa device to collect enough data.