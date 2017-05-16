# Configure Proximus cloudplatform


### Create CloudChannel

In orde to receive the uplink message and send the downlink message through Proximus, you need to ceate CloudChannel on [Proximus Enco cloudplatform](https://www.enco.io/).

1. Go to [EnCo DevPortal](http://devs.enco.io/dashboard/) and log in with your Enco account.
2. Navigate to CloudChannels API. You should get a overview of your CloudChannels API's.
3. Click on the button ![New CloudChannel](doc/img/new-cloudchannel.png) to create a new CloudChannel. You should see something like below:
	![Create CloudChannel](doc/img/create-cloudchannel.png)

4. Here you can define where the data come from and where the data should go. For this demo we use LoRa as the input and HTTP as the output.
5. After drag & drop required components to input and output, you need to configure LoRa inbound configuration, CloudChannel definition and HTTP outbound configuration.
6. Configure the input: 
	* Click on ![LoRa](doc/img/lora-inbound.png)
	* Select your device.
7. Configure CloudChannel definition: 
	* Click on Edit definition
	* Give CloudChannel a relevant name
	* Select TemperatureSensor
	* Click Ok
8. Configure the output:
	* Click on ![http](doc/img/http.png)
	* Give a relevant Name
	* Endpoint: http://[your ip]:8287/lora/api/proximus/uplink
	* HTTP method: POST
	* Click on Ok
9. Click Save Changes.
10. Redo above steps to create CloudChannels for PressureSensor, BatteryLevelSensor and HumiditySensor.