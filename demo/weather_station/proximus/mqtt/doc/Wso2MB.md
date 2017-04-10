# WSO2 Message Broker

### Content
* [Downloading the product](#download)
* [Running the server](#running)  
* [Setting up JAVA_HOME](#setting)


<a name="download"/>
## Downloading product
1. You can download the WSO2 Message Broker [here](http://wso2.com/products/message-broker/).
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

>  
__Note:__ All Wso2 products rely on the Java home variable. If you haven't installed Java you will need to install this first, otherwise the server will not run.  
>

<a name="setting"/>
## Setting up JAVA_HOME

Before you can use the server you need to add the JAVA_HOME variable to the environment variables. If you already have java 1.7 or 1.8 installed you can skip this part.

 * first you need to install java. the WSO2 DAS server only supports java 1.7 & 1.8
 * you can install the default java using the following line:  

 	```sudo apt-get install default-jdk```
 * once the default JDK is installed you can install the java version you want.
 I chose java 1.8  

 	```sudo apt-get install oracle-java8-installer```  

 * There can be multiple Java installations on one server. You can configure which version is the default for use in the command line by using update-alternatives, which manages which symbolic links are used for different commands.
 * This line will result in an table with all Java versions and there path. 

 	```sudo update-alternatives --config java```
 * now you need to set the JAVA_HOME variable using the following lines:

    ```
    export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
    export PATH=${JAVA_HOME}/bin:${PATH}
    ```  

 * To verify that the variable is set correctly, execute the following command:  
 		
 	```echo $JAVA_HOME```

## Using the product
When the Message Broker is running without an offset the Message Broker will listen for data packets on port 1883. By sending data to the server to this specific port with an topic, the server will automatically send the data to all subscribers of that topic.
