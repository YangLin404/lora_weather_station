# WSO2 Message Broker

### Content
* [Downloading the product](#download)
* [Running the server](#running)


<a name="download"/>
## Downloading product
1. [WSO2 Message Broker](http://wso2.com/products/message-broker/).
2. Unzip the file in the directory of your choice.  

<a name="running"/>
## Running the server 

* To start the server you need to run the wso2server.sh document in the bin directory. ```<wso2_MB_folder>/bin/```. you can run the server with this command once you are in the bin directory.

	```
	sh wso2server.sh
	```

* To stop the server, press Ctrl+C in the commmandline or shut the server down in the Management Console.
* to run the server in the background, use the following code:
	```
	sh wso2server.sh start
	sh wso2server.sh stop
	```  

## Using the product
When the Message Broker is running without an ofset the Message Broker will listen for data packets on port 1883. By sending data to the server on that port with an topic the server will automaticly send the data to all subscribers of that topic.