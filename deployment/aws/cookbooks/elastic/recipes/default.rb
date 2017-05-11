#
# Cookbook:: elastic
# Recipe:: default
#
# Copyright:: 2017, i8c, All Rights Reserved.


bash 'download elastic' do
	not_if { ::File.exist?('/home/lora/weather_station/temp/elasticsearch-5.2.2.zip')}
	cwd '/home/lora/weather_station/temp'
	code <<-EOH
	wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.2.2.zip
	EOH
	user 'lora'
	
end

bash 'install elastic' do
	not_if { ::File.exist?('/home/lora/weather_station/elasticsearch-5.2.2/bin/elasticsearch')}
	cwd '/home/lora/weather_station/temp'
	code <<-EOH
	unzip elasticsearch-5.2.2.zip -d ../
	EOH
	user 'lora'
end

bash 'start elastic' do
	not_if "ps aux|grep elasticsearc\[h\]"
	cwd '/home/lora/weather_station/'
	code <<-EOH
	elasticsearch-5.2.2/bin/elasticsearch -d
	EOH
	user 'lora'
end

bash 'download kibana' do
	not_if { ::File.exist?('/home/lora/weather_station/temp/kibana-5.2.2-linux-x86_64.tar.gz')}
	cwd '/home/lora/weather_station/temp'
	code <<-EOH
	wget https://artifacts.elastic.co/downloads/kibana/kibana-5.2.2-linux-x86_64.tar.gz
	EOH
	user 'lora'
end

bash 'install kibana' do
	not_if { ::File.exist?('/home/lora/weather_station/kibana-5.2.2-linux-x86_64/bin/kibana')}
	cwd '/home/lora/weather_station/temp'
	code <<-EOH
	tar -xzf kibana-5.2.2-linux-x86_64.tar.gz -C ../
	EOH
	user 'lora'
end

bash 'start kibana' do
	not_if "ps -aux | grep kiban\[a\]"
	cwd '/home/lora/weather_station/'
	code <<-EOH
	nohup kibana-5.2.2-linux-x86_64/bin/kibana < /dev/null > std.out 2> std.err &
	EOH
	user 'lora'
end

bash 'config Kibana' do
	cwd '/home/lora/weather_station/kibana-5.2.2-linux-x86_64/config'
	code <<-EOH
	sed -i 's/#server.host: "localhost"/server.host: "0.0.0.0"/' kibana.yml 
	EOH
	user 'lora'
end