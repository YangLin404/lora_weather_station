# Weather station with LoRa and Proximus

## Introduction

In this demo we will be building a weather station with loRa device as sender, Proximus as middleware handler, a msf4j based microservice as back-end. Both HTTP and MQTT protocol will be used in integration.

## Components
This demo contains following components: 

* [Arduino appication](arduino)

* Proximus through HTTP protocol
	* [LoRa micro service](../../../)

* Proximus through MQTT protocol
	* [MQTT](mqtt)