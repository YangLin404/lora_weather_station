# Automatic deployment on AWS

This Folder contains required instructions and files to automatically deploy lora microservice on [Amazon Web Services](https://aws.amazon.com/)


### Requirements

* [Vagrant](https://www.vagrantup.com/)
* [Vagrant aws plugin](https://github.com/mitchellh/vagrant-aws)
* Git


#### Step 1: install requirements

* download and install Vagrant at [download link](https://www.vagrantup.com/downloads.html)

* install vagrant aws plugin

	```shell
	vagrant plugin install vagrant-aws
	vagrant box add dummy https://github.com/mitchellh/vagrant-aws/raw/master/dummy.box
	```
### Step 2: Clone the source code

	```shell
	cd ~
	git clone -b loRa-rest-service --single-branch https://i8c.githost.io/wso2/loRa
	cd loRa/deployment
	```


### Step 2: configuration

* Open [config file](settings/setting.yaml) and edit where neccessary.
* Save config file

### Step 3: start

* start the deployment

	```shell
	vagrant up --provider=aws
	```
	
	
### Step 4: configure Kibana (If you are using Kibana)

* Follew [this instruction](../../demo/weather_station/ttn/msf4j/install/README.md#configureKibana).