#!/bin/bash -x


function checkAndInstallPostgresql()
{
	echo "checking postgresql"
	if ! type psql > /dev/null; then
		echo "postgresql not installed, install now";
		sudo apt-get install -y postgresql postgresql-contrib
		if [[ $? == 0 ]]; then
			echo "postgresql successfully installed";
		else
			echo "postgresql installation fails.";
			exit 1;
		fi
	else
		echo "postgresql already installed."
	fi
}

function configPostgresql()
{
	#sudo sed -i 's/peer/md5/' /etc/postgresql/9.5/main/pg_hba.conf 
	echo "copy config file"
	dir=/etc/postgresql/9.5/main/pg_hba.conf
	sudo yes | sudo cp ./postgresql/pg_hba.conf "$dir";
	sudo /etc/init.d/postgresql reload
}

function createDbAndUser()
{
	#sudo -u postgres psql template1 -c "alter user postgres password 'postgres';"
	echo "creating database and user";
	sudo -u postgres createdb loradb;
	sudo -u postgres psql template1 -c "CREATE USER lora WITH PASSWORD 'lora';"
	sudo -u postgres psql template1 -c "GRANT ALL PRIVILEGES ON DATABASE loradb to lora;"
}

checkAndInstallPostgresql;
configPostgresql;
createDbAndUser;
if [[ $? == 0 ]]; then
	exit 0;
fi

