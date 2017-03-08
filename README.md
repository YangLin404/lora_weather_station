#LoRa RestApi with MSF4J and postgresql

## Introduction

For this project, 2 [LORAWAN Rapid Development Kits](http://www.allthingstalk.com/lorawan-rapid-development-kit) are available from www.allthingstalk.com.  

These kits come with access to the following websites:
* https://maker.allthingstalk.com  
* https://devs.enco.io/dashboard  
username = kristof.lievens@i8c.be  
password = L0Ra2017!  

As a first step, follow these tutorial to get up and running:
* http://docs.allthingstalk.com/tutorials/setup-lora-rapid-development-kit/  
* http://docs.enco.io/docs/getting-started-with-enco  

## Functional Specs
The purpose of this application is to build a RESTApi server which stores the data's received by LoRa device through Proximus Enco.

## Technical Specs
### Architecture
This solution contains the following components:
* a REST micro service, based on WSO2 MSF4J and Spring, storing its data in a Postgresql database.
* a sketch application which allows loRa device to send data.

### Dependencies
#### RestApi server
Git
JDK
Maven
WSO2 MSF4J
Postgresql

#### sketch application
Arduino Sodaq Mbili Library



