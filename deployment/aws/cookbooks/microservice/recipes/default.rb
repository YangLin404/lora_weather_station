#
# Cookbook:: microservice
# Recipe:: default
#
# Copyright:: 2017, i8c, All Rights Reserved.

database = node['lora']['database']
protocol = node['lora']['protocol']
middleware = node['lora']['middleware_handler']

bash 'build project' do
	cwd '/home/lora/git/demo/weather_station/common/src/msf4j'
	code <<-EOH
	mvn clean package
	EOH
	user 'lora'
end

template '/home/lora/git/demo/weather_station/common/src/msf4j/target/config/application.properties' do
	source 'application.properties.erb'
	owner 'lora'
	variables({
		elasticIndex: node['lora']['elasticIndex'],
		elasticHost: node['lora']['elasticHost'],
		elasticPort: node['lora']['elasticPort'],
		dbUrl: node['lora']['dbUrl'],
		dbUsername: node['lora']['dbUsername'],
		dbPassword: node['lora']['dbPassword'],
		ttnRegion: node['lora']['ttnRegion'],
		ttnAppId: node['lora']['ttnAppId'],
		ttnAccessKey: node['lora']['ttnAccessKey'],
		ttnHttpUrl: node['lora']['ttnHttpUrl'],
		ttnDeviceFormat: node['lora']['ttnDeviceFormat'],
		proximusTokenUrl: node['lora']['proximusTokenUrl'],
		proximusApiKey: node['lora']['proximusApiKey'],
		proximusApiSecret: node['lora']['proximusApiSecret'],
		proximusDownlinkUrl: node['lora']['proximusDownlinkUrl'],
		proximusDeviceId: node['lora']['proximusDeviceId'],
		})
end

ruby_block 'waiting elastic server' do
	block do
		true until system("netstat -ntl | grep 9200")
	end
	only_if { node['lora']['database'] == "elastic"}
end


bash 'start microservice ttn' do
	not_if "ps -aux | grep msf4\[j\]"
	not_if { node['lora']['middleware_handler'] == "proximus"}
	#only_if "until netstat -ntl | grep 9200; do echo 'waiting for elasticsearch...'; sleep 2; done", :timeout => 20
	cwd '/home/lora/git/demo/weather_station/common/src/msf4j/target'
	code <<-EOH
	nohup java -jar -Dspring.profiles.active=#{database},#{protocol} msf4j-0.1-SNAPSHOT.jar < /dev/null > std.out 2> std.err &
	EOH
	user 'lora'
end

bash 'start microservice proximus' do
	not_if "ps -aux | grep msf4\[j\]"
	not_if { node['lora']['middleware_handler'] == "ttn"}
	#only_if "until netstat -ntl | grep 9200; do echo 'waiting for elasticsearch...'; sleep 2; done", :timeout => 20
	cwd '/home/lora/git/demo/weather_station/common/src/msf4j/target'
	code <<-EOH
	nohup java -jar -Dspring.profiles.active=#{database},#{middleware} msf4j-0.1-SNAPSHOT.jar < /dev/null > std.out 2> std.err &
	EOH
	user 'lora'
end