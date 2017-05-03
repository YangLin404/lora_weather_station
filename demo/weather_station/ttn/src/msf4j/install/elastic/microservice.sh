#!/bin/bash
PATHTOSRC="../../";
JARPATH="target/msf4j-0.1-SNAPSHOT.jar";
ELASTICPROFILE="-Dspring.profiles.active=elasticsearch";


function installMaven()
{
	echo "installing maven"
	sudo apt-get install maven;
	if [[ $? != 0 ]]; then
		echo "install maven fails";
		exit 1;
	fi
}

function installElastic()
{
	echo "installing elasticsearch";
	wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.2.2.zip;
	if [[ $? != 0 ]]; then
		echo "download elasticsearch fails";
		exit 1;
	fi
	unzip elasticsearch-5.2.2.zip 1> /dev/null;
	if [[ $? != 0 ]]; then
		echo "unzip elasticsearch fails"
		exit 1;
	fi
}

function installKibana()
{
	echo "installing Kibana";
	wget https://artifacts.elastic.co/downloads/kibana/kibana-5.2.2-linux-x86_64.tar.gz;
	if [[ $? != 0 ]]; then
		echo "download kibana fails";
		exit 1;
	fi
	tar -xzf kibana-5.2.2-linux-x86_64.tar.gz 1> /dev/null;
	if [[ $? != 0 ]]; then
		echo "unzip kibana fails"
		exit 1;
	fi
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
	#check maven
	if ! type mvn > /dev/null; then
		echo "mvn not installed";
		installMaven;
	else
		echo "mvn installed";
	fi

	#check jdk
	if ! type javac > /dev/null; then
		echo "jdk not installed";
		echo "use 'sudo sudo apt-get install openjdk-8-jdk;' to install jdk and run try again"
		exit 1;
	else
		echo "jdk installed";
	fi
	#check elasticsearch
	if [[ -f ./elasticsearch-5.2.2/bin/elasticsearch ]]; then
		echo "elasticsearch installed";
	else
		echo " elasticsearch not installed";
		installElastic;
	fi
	#check kibana
	if [[ -f ./kibana-5.2.2-linux-x86_64/bin/kibana ]]; then
		echo "kibana installed";
	else
		echo " kibana not installed";
		installKibana;
	fi
}

function checkConfig()
{
	sed -i 's/#server.host: "localhost"/server.host: "0.0.0.0"/' ./kibana-5.2.2-linux-x86_64/config/kibana.yml;
}

function checkParameter()
{
	for par in "$@"
	do
		if [[ "$par" == "http" ]]; then
			ELASTICPROFILE += ",http";
		elif [[ "$par" == "mqtt" ]]; then
			ELASTICPROFILE += ",mqtt"
		fi
	done
}

if [[ "$1" == "--install" ]]; then
	checkRequirements;
	checkConfig;
	build;
	echo "-------------------------------------------------------------------------------";
	echo "installation is completed, run this script with --start to start the micro service";
elif [[ "$1" == "--start" ]]; then
	checkConfig;
	checkParameter;
	echo "starting elasticsearch";
	./elasticsearch-5.2.2/bin/elasticsearch -d;
	echo "waiting elasticsearch to start.....";
	sleep 5;
	echo "starting kibana";
	./kibana-5.2.2-linux-x86_64/bin/kibana 1>/dev/null  &
	disown;
	echo "starting REST API server";
	java -jar "$ELASTICPROFILE" "$PATHTOSRC$JARPATH";	
elif [[ "$1" == "--stop" ]]; then
	echo "Finding pids of elasticsearch and kibana server..."
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
fi
