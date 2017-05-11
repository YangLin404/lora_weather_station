#
# Cookbook:: lora_microservice
# Recipe:: default
#
# Copyright:: 2017, i8c, All Rights Reserved.


apt_update 'Update the apt cache daily' do
  frequency 86_400
  action :periodic
end


package 'unzip'
package 'openjdk-8-jdk'
package 'maven'

user 'user lora' do
	username 'lora'
	password '$1$Z4v3/Vcd$RGPhQcSSumhxpA7xAqh1u.'
	home '/home/lora'
	manage_home true
	system false
	shell '/bin/bash'
	action :create
end

%w[ /home/lora/git /home/lora/.ssh /home/lora/weather_station /home/lora/weather_station/temp /home/lora/.m2].each do |path|
	directory path do
		owner 'lora'
		mode '0755'
	end
end

cookbook_file '/home/lora/.ssh/id_rsa' do
  source 'id_rsa'
  mode '0400'
  owner 'lora'
  action :create
end

cookbook_file '/home/lora/.ssh/id_rsa.pub' do
  source 'id_rsa.pub'
  mode '0400'
  owner 'lora'
  action :create
end

cookbook_file '/home/lora/.ssh/known_hosts' do
  source 'known_hosts'
  mode '0400'
  owner 'lora'
  action :create
end

git '/home/lora/git' do
	repository 'git@i8c.githost.io:wso2/loRa.git'
	reference 'loRa-rest-service'
	revision 'loRa-rest-service'
	user 'lora'
	action :sync
end

#only for debugging
=begin
bash 'copy maven repo' do
	not_if { ::File.directory?('/home/lora/.m2/repository')}
	cwd '/home/vagrant/shared'
	code <<-EOH
	cp -R repository /home/lora/.m2/
	EOH
	user 'lora'
end
=end