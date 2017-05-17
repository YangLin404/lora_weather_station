#!/bin/bash
PATHTOSRC="../";
BUILD="true";
MAINPATH="~/lora/"

JARPATH="msf4j-0.1-SNAPSHOT.jar";
SPRINGPROFILE="-Dspring.profiles.active=";
BACKGROUND="< /dev/null > std.out 2> std.err &";
runInbg="false";
profileAdded="false";
PACKAGENAME="msf4j-0.1-SNAPSHOT";


function installMaven()
{
	echo "installing maven"
	sudo apt-get install -y maven;
	if [[ $? != 0 ]]; then
		echo "install maven fails";
		exit 1;
	fi
}

function installSDK()
{
	echo "installing java sdk"
	sudo apt-get install -y openjdk-8-jdk;
	if [[ $? != 0 ]]; then
		echo "install java sdk fails";
		exit 1;
	fi
}

function installUnzip()
{
	echo "install unzip";
	sudo apt-get install unzip;
}

function installUnzip()
{
	echo "installing unzip";
	sudo apt-get install -y unzip;
}

function build()
{
	echo "start building project"
	cd "$PATHTOSRC";
	mvn package;
	if [[ $? != 0 ]]; then
		echo "build project fails."
		exit 1;
	fi
	cd ./install/elastic;
}

function checkRequirements()
{
	#check jdk
	if ! type javac > /dev/null; then
		echo "jdk not installed";
		installSDK;
	else
		echo "jdk installed";
	fi

	#check maven
	if ! type mvn > /dev/null; then
		echo "mvn not installed";
		installMaven;
	else
		echo "mvn installed";
	fi

	#check unzip
	if ! type unzip > /dev/null; then
		echo "unzip not installed";
		installUnzip;
	else
		echo "unzip installed"
	fi
}

function startElasticInBG()
{
	echo "start elastic in background"
	eval "nohup ~/lora/elasticsearch-5.2.2/bin/elasticsearch $BACKGROUND";
}

function startElastic()
{
	echo "start elastic"
	~/lora/elasticsearch-5.2.2/bin/elasticsearch -d;
}

function startKibanaInBG()
{
	echo "start kibana"
	eval "nohup ~/lora/kibana-5.2.2-linux-x86_64/bin/kibana $BACKGROUND";

}


function checkElastic()
{
	pidElastic=`ps aux|grep elasticsearc\[h\] | awk {'print $2'}`;
	if [[ -z "$pidElastic" ]]; then
		echo "elasticsearch is not running. starting now";
		startElasticInBG;
		
	else
		echo "elasticsearch is running";
	fi
}

function checkKibana()
{
	pidKibana=`ps aux|grep kiban\[a\] | awk {'print $2'}`;
	if [[ -z "$pidKibana" ]]; then
		echo "kibana is not running. starting now";
		startKibanaInBG;
	else
		echo "kibana is running";
	fi
}

function checkJar()
{
	pidJar=`ps aux|grep msf4j-0.1-SNAPSHO\[T\] | awk {'print $2'}`;
	if [[ -z "$pidJar" ]]; then
		echo "microservice is not running. starting now";
		startJar;
	else
		echo "microservice is running";
	fi
}

function startJarInBG()
{
	cd "${PATHTOSRC}/target";
	eval nohup java -jar "$SPRINGPROFILE $JARPATH $BACKGROUND";
}

function startJar()
{
	if [[ "$runInbg" = "true" ]]; then
		startJarInBG;
	else
		cd "${PATHTOSRC}/target";
		eval java -jar "$SPRINGPROFILE $JARPATH";
	fi	
}

function stopElastic()
{
	echo "Finding pids of elasticsearch server..."
	pidElastic=`ps aux|grep elasticsearc\[h\] | awk {'print $2'}`;
	if [[ -z "$pidElastic" ]]; then
		echo "elasticsearch is not running."
	else
		echo "pid of elasticsearch: $pidElastic";
		kill "$pidElastic";
		if [[ $?==0 ]]; then
			echo "elasticsearch server stopped."
		else
			echo "stopping elasticsearch server fails."
		fi
	fi
}

function stopKibana()
{
	pidKibana=`ps aux|grep kiban\[a\] | awk {'print $2'}`
	if [[ -z "$pidKibana" ]]; then
		echo "kibana is not running."
	else
		echo "pid of kibana: $pidKibana";
		kill "$pidKibana";
		if [[ $?==0 ]]; then
			echo "kibana server stopped."
		else
			echo "stopping kibana server fails."
		fi
	fi
}

function stopJar()
{
	echo "Finding pids of microservice";
	pidJar=`ps -aux | grep msf4\[j\] | awk {'print $2'}`;
	if [[ -z "$pidJar" ]]; then
		echo "microservice is not running."
	else
		echo "pid of microservice: $pidJar";
		kill "$pidJar";
		if [[ $?==0 ]]; then
			echo "microservice server stopped."
		else
			echo "stopping microservice server fails."
		fi
	fi
}

function checkParameter()
{
	for par in "$@"
	do
		if [[ "$par" == "nobuild" ]]; then
			BUILD="false";
		elif [[ "$par" == "bg" ]]; then
			runInbg="true";
		elif [[ "$par" == "elastic" ]]; then
			if [[ "$profileAdded" == "false" ]]; then
				SPRINGPROFILE+="elastic";
				profileAdded="true"
			else
				SPRINGPROFILE+=",elastic";
			fi
		elif [[ "$par" == "mqtt" ]]; then
			if [[ "$profileAdded" == "false" ]]; then
				SPRINGPROFILE+="mqtt";
				profileAdded="true"
			else
				SPRINGPROFILE+=",mqtt";
			fi
		elif [[ "$par" == "http" ]]; then
			if [[ "$profileAdded" == "false" ]]; then
				SPRINGPROFILE+="http";
				profileAdded="true"
			else
				SPRINGPROFILE+=",http";
			fi
		elif [[ "$par" == "postgresql" ]]; then
			if [[ "$profileAdded" == "false" ]]; then
				SPRINGPROFILE+="postgresql";
				profileAdded="true"
			else
				SPRINGPROFILE+=",postgresql";
			fi
		elif [[ "$par" == "proximus" ]]; then
			if [[ "$profileAdded" == "false" ]]; then
				SPRINGPROFILE+="proximus";
				profileAdded="true"
			else
				SPRINGPROFILE+=",proximus";
            fi
		fi
	done
}

checkParameter "$@";
checkRequirements;
if [[ "$1" == "--install" ]]; then
	if [[ "$2" == "elastic" ]]; then
		chmod +x ./elastic/install.sh;
		./elastic/install.sh;
	elif [[ "$2" == "postgresql" ]]; then
		chmod +x ./postgresql/install.sh;
		./postgresql/install.sh;
	else
		echo "elasticsearch or postgresql? example: ./microservice.sh --install elastic for elasticsearch";
		exit 1;
	fi
	if [[ $? == 0 ]]; then
		if [[ "$BUILD" == "true" ]]; then
			build;	
		fi
		echo "-------------------------------------------------------------------------------";
		echo "installation is completed, run this script with --start to start the micro service";
	else
		echo "install fails.";
		exit 1;
	fi

elif [[ "$1" == "--start" ]]; then
	checkElastic;
	checkKibana;
	checkJar;	
elif [[ "$1" == "--stop" ]]; then
	stopElastic;
	stopJar;
	stopKibana;
else
	echo "no parameter";
	exit 1;
fi
