#!/bin/bash
PATHTOSRC="../../";
JARPATH="target/msf4j-0.1-SNAPSHOT.jar";
ELASTICPROFILE="-Dspring.profiles.active=elasticsearch";


function downloadElastic()
{
	wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.2.2.zip -P ~/lora;
	if [[ $? != 0 ]]; then
		echo "download elasticsearch fails";
		exit 1;
	fi
}

function installElastic()
{
	echo "installing elasticsearch";
	if [[ ! -f ~/lora/elasticsearch-5.2.2.zip ]]; then
		downloadElastic;
	fi
	unzip ~/lora/elasticsearch-5.2.2.zip -d ~/lora 1> /dev/null;
	if [[ $? != 0 ]]; then
		echo "unzip elasticsearch fails"
		exit 1;
	fi
}

function downloadKibana()
{
	wget https://artifacts.elastic.co/downloads/kibana/kibana-5.2.2-linux-x86_64.tar.gz -P ~/lora;
	if [[ $? != 0 ]]; then
		echo "download kibana fails";
		exit 1;
	fi
}

function installKibana()
{
	echo "installing Kibana";
	if [[ ! -f  ~/lora/kibana-5.2.2-linux-x86_64.tar.gz ]]; then
		downloadKibana;
	fi
	sudo tar -xzf ~/lora/kibana-5.2.2-linux-x86_64.tar.gz -C ~/lora 1> /dev/null;
	if [[ $? != 0 ]]; then
		echo "unzip kibana fails"
		exit 1;
	fi
}

function checkRequirements()
{
	#check elasticsearch
	if [[ -f ~/lora/elasticsearch-5.2.2/bin/elasticsearch ]]; then
		echo "elasticsearch installed";
	else
		echo " elasticsearch not installed";
		installElastic;
	fi
	#check kibana
	if [[ -f ~/lora/kibana-5.2.2-linux-x86_64/bin/kibana ]]; then
		echo "kibana installed";
	else
		echo " kibana not installed";
		installKibana;
	fi
}

function checkConfig()
{
	sed -i 's/#server.host: "localhost"/server.host: "0.0.0.0"/' ~/lora/kibana-5.2.2-linux-x86_64/config/kibana.yml;
}
	checkRequirements;
	checkConfig;
