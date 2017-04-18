## LoRa micro service

LoRa micro service is a REST based micro service for receiving en storing the loRa packets forwarded by The Things network(TTN) back-end. It is build upon [MSF4j](https://github.com/wso2/msf4j) and [Spring](https://spring.io). In addition, it's also used to manage the connections with TTN back-end. In that case, HTTP request is used.

### Installing

Please read [INSTALL.md](install/INSTALL.md) for instruction.

### <a name="apireferrences">API referrences</a>

##### 1. MQTT client administration 

| Method | HTTP Request | Description |
|---|---|---|
| start  | POST /api/ttn/manage/start  | start the mqtt client of TTN  |
| stop  | POST /api/ttn/manage/stop  | stop the mqtt client of TTN  |

