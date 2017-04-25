# Installing LoRa micro service
Follow steps below to get LoRa micro service running.
## <a name="Prerequirements"></a> Requirements
* Git
* JDK 1.8 or above
* Maven

### <a name="step1">Step 1: Clone the source code</a>

Clone the source code by using following commands:

```shell
cd ~
mkdir lora-ttn
cd lora-ttn
git clone -b loRa-rest-service-ttn --single-branch https://i8c.githost.io/wso2/loRa
```

### Step 2: Installation

The next step is to setup the back-end on your Server.

* For server using elasticsearch and Kibana please follow: [install with elasticsearch](elastic/INSTALL-elastic.md).
* For server using postgresql please follow: [install with postgresql](postgresql/INSTALL-postgresql.md) (Note. The tool for analyzing is currently not provided).

### Step 3: All done

##### All done! Now just wait for your loRa device to collect enough data.