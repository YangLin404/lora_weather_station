# Introduction

For this project, 2 [LORAWAN Rapid Development Kits](http://www.allthingstalk.com/lorawan-rapid-development-kit) are available from www.allthingstalk.com.  

These kits come with access to the following websites:  

* https://maker.allthingstalk.com  
* https://devs.enco.io/dashboard  
username = kristof.lievens@i8c.be  
password = L0Ra2017!  

## This Demo

This demo sends data trough the LoRa network to an WSO2 Message Broker that receives data from the Proximus Enco cloud platform, the Message Broker will send the data to an WSO2 Data Analytics Server that will analyse, store and display the data. As shown in the diagram bellow:   
![Demo scheme](Doc/img/StageDiagramProximusEngels.jpg)  

### Step 1  

As a first step, follow these tutorial to get up and running:

* http://docs.allthingstalk.com/tutorials/setup-lora-rapid-development-kit/  
* http://docs.enco.io/docs/getting-started-with-enco

### Step 2

Once you acctivated your device you need to setup the Message Broker. When the Message Broker is up and running you can configure the cloud channels so that, when data is received, the cloudplatform will forward the data to the Message Broker.  
You can follow [this guide](Doc/Wso2MB.md) for setting up the WSO2 Message Broker.

### Step 3  

The next step is to set up the WSO2 Data Analytic Server.  
For setting up the WSO2 DAS you can follow [this guide](Doc/Wso2DAS.md).  

### Step 4

The final step is to create an dashboard so you can vieuw the stored, analysed and stored data. You can learn how to create an dashboard in [this guide](Doc/loldoesnotexistyet.md).