#
# Cookbook:: elastic
# Recipe:: default
#
# Copyright:: 2017, i8c, All Rights Reserved.

package 'postgresql'


cookbook_file '/etc/postgresql/9.5/main/pg_hba.conf' do
  source 'pg_hba.conf'
  mode '0640'
  owner 'postgres'
  group 'postgres'
  action :create
end

bash 'reload postgres' do
	code <<-EOH
	/etc/init.d/postgresql reload
	EOH
end

bash 'create postgresql db and user' do
	code <<-EOH
	sudo -u postgres createdb loradb
	sudo -u postgres psql template1 -c "CREATE USER lora WITH PASSWORD 'lora';"
	sudo -u postgres psql template1 -c "GRANT ALL PRIVILEGES ON DATABASE loradb to lora;"
	EOH
end