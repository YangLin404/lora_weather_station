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
The purpose of this application is to build a RESTApi micro service which receives the data's from LoRa device, then stores it into a stand-alone elasticsearch server

## Technical Specs
### Architecture
This solution contains the following components:  
* a REST micro service, based on WSO2 MSF4J and Spring, storing its data into a stand-alone elasticsearch server 
* a sketch application which allows loRa device to send data.

### Dependencies
1. ##### RestApi server
	* Git  
	* JDK  
	* Maven  
	* WSO2  
	* MSF4J
	* Elasticsearch Java Api   

2. ##### sketch application
	* Arduino Sodaq Mbili Library

3. ##### elasticsearch 5.2.2
	* download from [Here](https://www.elastic.co/downloads/elasticsearch) 

4. ##### kibana 5.2.2
	* download from [Here](https://www.elastic.co/downloads/kibana) 

## Installation on Ubuntu Server 16.04 LTS

To be done

### Installation  for RestApi:

To be done


    

