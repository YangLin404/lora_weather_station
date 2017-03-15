# Installing WSO2 DAS server
## Downloading product
1. [WSO2 Data Analytics Server Download page](http://wso2.com/smart-analytics#iBottom).

2. Unzip the file in the directory of your choise.

## Setting up JAVA_HOME

* Before you can use the server you need to add the JAVA_HOME varible to the envirment variables.
 * first you need to install java. the WSO2 DAS server only supports java 1.7 & 1.8
 * you can install the default java jusing the following line:  

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

 * To verify that the varible is set correctly, execute the following command:  
 		
 		```echo $JAVA_HOME```

##Running the server 

* To start the server you need to run the wso2server.sh document in the bin directory. ```<wso2_das_folder>/bin/```. you can run the server with this command once you are in the bin directory.

	```
	sh wso2ser.sh
	```

* To stop the server, press Ctrl+C in the commmandline or shut the server down in the Managemant Console.

* to run the server in the background, use the following code:
	```
	sh wso2server.sh start
	sh wso2server.sh stop
	```

## Accessing the Managment Console

* Once the server is started, u can acces the managment console by openening the URL that is given in the console. The URL will look like this:

	```https://<server host>:9443/carbon/```
* U can sing-in in the server using admin as both the username and password.

##Wso2 DAS Architecture

the Wso2 DAS architecture is shown in the illustration below.
![Wso2 DAS architecture](img/WSO2_DAS_Architecture)