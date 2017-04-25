# Configuration
All configurations are required, unless otherwise specified.

## General

| Config |  Description | Default |
|---|---|---|
| **http.port**  | the port which microservice will be listening. | 8287  |

## Elasticsearch

| Config | Description | Default |
|---|---|---|
| **elasticsearch.index** | the name of elasticsearch index where the data will be stored. Must be lowercase. | lora-ttn |
| **elasticsearch.host** | the hostname or ip where elasticsearch server can be found. | localhost |
| **elasticsearch.port** | the port which elasticsearch is listening. | 9300 |

## Postgresql

| Config | Description | Default |
|---|---|---|
| **db.driver** | name of driver class for postgresql | org.postgresql.Driver |
| **db.url** | url of your postgresql database | |
| **db.username** | username of postgresql database | |
| **db.password** | password of postgresql database | |

## Hibernate

| Config | Description | Default |
|---|---|---|
| **hibernate.dialect** | This property makes Hibernate generate the appropriate SQL for the chosen database | org.hibernate.dialect.PostgreSQLDialect |
| **hibernate.hbm2ddl.auto** | Automatically validates or exports schema DDL to the database when the SessionFactory is created. | update |
| **hibernate.show_sql** | enable the logging of all the generated SQL statements to the console. | false |

## The thingsnetwork

| Config | Description | Default |
|---|---|---|
| **ttn.region** | The region (e.g. eu) or full hostname (e.g. eu.thethings.network) of the handler to connect to. | eu |
| **ttn.appId** | The ID of the application to connect to. |
| **ttn.accessKey** | An access key for the application, formatted as base64. |

## Devices:


| Config | Description | Default |
|---|---|---|
| **device.deviceid** | The deviceId of loRa devices, separated by ; (e.g. device1;device2). | |
| **device.format** | The payload format of device, separated by ; (e.g. Temperature,Light;Pressure,BatteryLevel defines the payload formats for device1 and device2 above respectively).