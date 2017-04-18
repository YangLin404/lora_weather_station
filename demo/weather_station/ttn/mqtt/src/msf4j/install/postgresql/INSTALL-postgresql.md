# LoRa REST based micro service with Postgresql

## Requirements
* Postgresql

## Installation on Ubuntu Server 16.04 LTS

Make sure you have installed requirement listed above.

### Step 1. Change postgresql authentication methode to md5
Open config file
```shell
sudo vim /etc/postgresql/<version>/main/pg_hba.conf 
```

Find:

```txt
# "local" is for Unix domain socket connections only
local   all             all                                     peer
# IPv4 local connections:
host    all             all             127.0.0.1/32            peer
# IPv6 local connections:
host    all             all             ::1/128                 peer
```
change peer to md5

```txt
# "local" is for Unix domain socket connections only
local   all             all                                     md5
# IPv4 local connections:
host    all             all             127.0.0.1/32            md5
# IPv6 local connections:
host    all             all             ::1/128                 md5
```

### Step 2. Reload postgresql config
```shell
sudo /etc/init.d/postgresql reload
```
### Step 3. Create postgresql database 'loRaDb'
```shell
sudo -u postgres createdb loRaDb
```
### Step 4. Create postgresql user 'loRa' with password 'loRa'
```shell
sudo -u postgres psql template1
```
```sql
CREATE USER loRa WITH PASSWORD 'loRa';
GRANT ALL PRIVILEGES ON DATABASE loRaDb to loRa;
```
### Step 5. Build project from source code

* Go to the directory where you have clone the source code in this [step](../INSTALL.md#step1).

* Build source code

	```shell
	cd loRa/demo/weather_station/ttn/mqtt/src/msf4j/
	mvn package
	```
	
### Step 6. Run micro service

```shell
java -jar -Dspring.profiles.active=postgresql ./target/msf4j-0.1-SNAPSHOT.jar
```



    



