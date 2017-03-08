# LoRa RestApi

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
1. ##### RestApi server
	* Git  
	* JDK  
	* Maven  
	* WSO2  
	* MSF4J
	* Postgresql   

2. ##### sketch application
	* Arduino Sodaq Mbili Library

## Installation on Ubuntu Server 16.04 LTS

The following dependencies need to be installed:
* JDK: sudo apt install openjdk-8-jdk-headless
* Maven: sudo apt install maven
* Postgresql: sudo apt-get install postgresql postgresql-contrib


### Installation for RestApi:
Step 1. Create postgresql database 'loRaDb'
```shell
sudo -u postgres createdb loRaDb
```
Step 2. Create postgresql user 'loRa' with password 'loRa'
```shell
sudo -u postgres psql template1
```
```sql
CREATE USER loRa WITH PASSWORD 'loRa';
GRANT ALL PRIVILEGES ON DATABASE loRaDb to loRa;
```
Step 3. Build project from source code
```shell
cd ~
madir loRa
cd loRa
git init
git pull https://i8c.githost.io/wso2/loRa/tree/loRa-RestService-yanglin
cd services/msf4j
mvn package
```
Step 4. Start RestApi
```shell
cd target/
java -jar msf4j-0.1-SNAPSHOT
```


    

