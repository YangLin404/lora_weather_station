# Weather station with LoRa and The Things network(TTN)

## Introduction

In this demo we will be building a weather station with loRa device as sender and The Thingsnetwork as middleware handler. Both HTTP and MQTT protocol will be used in integration.

## Components
### * [LoRa micro service](src/msf4j/README.md)
LoRa micro service is a REST based micro service for receiving en storing the loRa packets forwarded by The Things network(TTN) back-end. It is build upon MSF4j and Spring. It could also be used to manage the connections with TTN back-end. In that case, HTTP request is used. In addiction, it also provides the ability to send the downlink message to the specific loRa device.
### * Arduino 

## Installing
#### * LoRa micro service

Please read [INSTALL.md](src/msf4j/install/INSTALL.md) for instruction.

#### * Arduino