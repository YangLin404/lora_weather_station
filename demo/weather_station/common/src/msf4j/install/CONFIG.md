# Configuration
All configurations are required, unless otherwise specified.

## General

| Config |  Description | Default |
|---|---|---|
| **http.port**  | the port which microservice will be listening. | 8287  |

## Elasticsearch
This configurations is used by elasticsearch. Leave it empty if you are using Postgresql database.

| Config | Description | Default |
|---|---|---|
| **elasticsearch.index** | the name of elasticsearch index where the data will be stored. Must be lowercase. | lora |
| **elasticsearch.host** | the hostname or ip where elasticsearch server can be found. | localhost |
| **elasticsearch.port** | the port which elasticsearch is listening. | 9300 |

## Postgresql
This configurations is used by postgresql database. Leave it empty if you are using elasticsearch.

| Config | Description | Default |
|---|---|---|
| **db.driver** | name of driver class for postgresql | org.postgresql.Driver |
| **db.url** | url of your postgresql database | |
| **db.username** | username of postgresql database | |
| **db.password** | password of postgresql database | |

## Hibernate
This configurations is used by postgresql database. Leave it empty if you are using elasticsearch.

| Config | Description | Default |
|---|---|---|
| **hibernate.dialect** | This property makes Hibernate generate the appropriate SQL for the chosen database | org.hibernate.dialect.PostgreSQLDialect |
| **hibernate.hbm2ddl.auto** | Automatically validates or exports schema DDL to the database when the SessionFactory is created. | update |
| **hibernate.show_sql** | enable the logging of all the generated SQL statements to the console. | false |
| **hibernate.ejb.naming_strategy** | naming strategy | org.hibernate.cfg.ImprovedNamingStrategy |
| **hibernate.format_sql** | sql format | true|

## The thingsnetwork(not required if you are using proximus):

| Config | Description | Default |
|---|---|---|
| **ttn.region** | The region (e.g. eu) or full hostname (e.g. eu.thethings.network) of the handler to connect to. | eu |
| **ttn.appId** | The ID of the application to connect to. |
| **ttn.accessKey** | An access key for the application, formatted as base64. |
| **device.deviceid** | The deviceId of loRa devices, separated by ; (e.g. device1;device2). | |
| **device.format** | The payload format of device, separated by ; (e.g. Temperature,Light;Pressure,BatteryLevel defines the payload formats for device1 and device2 above respectively). |

## Proximus(not required if you are using the thingsnetwork):

this configurations is specific for proximus. For more info, please visit [proximus doc](http://docs.enco.io/docs/)

| Config | Description | Default |
|---|---|---|
| proximus.tokenAPIUrl | The url of oauth2 api provided by [Proximus](http://docs.enco.io/docs/authentication-1) | https://api.enco.io/token |
| proximus.APIKey | application keys provided by [Proximus](http://docs.enco.io/docs/authentication-1) | |
| proximus.APISecret | api secret provided by [Proximus](http://docs.enco.io/docs/authentication-1) | |
| proximus.downlinkUrl | url of api for sending downlink message provided by [Proximus](http://docs.enco.io/docs/lora-downlink-api) | https://api.enco.io/seaas/0.0.1 |
| proximus.devic e.deviceid | The deviceId of loRa devices, separated by ; (e.g. device1;device2). |