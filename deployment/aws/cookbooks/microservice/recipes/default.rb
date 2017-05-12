#
# Cookbook:: ttn
# Recipe:: default
#
# Copyright:: 2017, i8c, All Rights Reserved.

database = node['lora']['database']
protocol = node['lora']['protocol']
middleware = node['lora']['middleware_handler']

bash 'build project ttn' do
	cwd '/home/lora/git/demo/weather_station/ttn/msf4j'
	code <<-EOH
	mvn clean package
	EOH
	user 'lora'
	not_if { node['lora']['middleware_handler'] == "proximus"}
end

bash 'build project proximus' do
	cwd '/home/lora/git/demo/weather_station/proximus/http/src/msf4j'
	code <<-EOH
	mvn clean package
	EOH
	user 'lora'
	not_if { node['lora']['middleware_handler'] == "ttn"}
end

ruby_block 'waiting elastic server' do
	block do
		true until system("netstat -ntl | grep 9200")
	end
	only_if { node['lora']['database'] == "elastic"}
end


bash 'start microservice ttn' do
	not_if "ps -aux | grep msf4\[j\]"
	not_if { node['lora']['database'] == "postgresql"}
	not_if { node['lora']['middleware_handler'] == "proximus"}
	#only_if "until netstat -ntl | grep 9200; do echo 'waiting for elasticsearch...'; sleep 2; done", :timeout => 20
	cwd '/home/lora/git/demo/weather_station/ttn/msf4j/target'
	code <<-EOH
	nohup java -jar -Dspring.profiles.active=#{database},#{protocol} msf4j-0.1-SNAPSHOT.jar < /dev/null > std.out 2> std.err &
	EOH
	user 'lora'
end

bash 'start microservice proximus' do
	not_if "ps -aux | grep msf4\[j\]"
	not_if { node['lora']['database'] == "postgresql"}
	not_if { node['lora']['middleware_handler'] == "ttn"}
	#only_if "until netstat -ntl | grep 9200; do echo 'waiting for elasticsearch...'; sleep 2; done", :timeout => 20
	cwd '/home/lora/git/demo/weather_station/proximus/http/src/msf4j/target'
	code <<-EOH
	nohup java -jar -Dspring.profiles.active=#{database},#{middleware} msf4j-0.1-SNAPSHOT.jar < /dev/null > std.out 2> std.err &
	EOH
	user 'lora'
end