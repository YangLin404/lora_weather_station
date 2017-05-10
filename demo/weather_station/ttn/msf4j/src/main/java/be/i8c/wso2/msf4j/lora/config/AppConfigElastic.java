package be.i8c.wso2.msf4j.lora.config;

import be.i8c.wso2.msf4j.lora.models.SensorBuilder;
import be.i8c.wso2.msf4j.lora.models.SensorRecord;
import be.i8c.wso2.msf4j.lora.models.SensorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by yanglin on 10/05/17.
 */


@Configuration
public class AppConfigElastic {
    private static final Logger logger = LogManager.getLogger(AppConfigElastic.class);

    /**
     * The hostname of server where elasticseach can be found. It will be automatically read from application.properties
     */
    @Value("${elasticsearch.host}")
    private String esHost;

    /**
     * The name of index. It will be automatically read from application.properties
     */
    @Value("${elasticsearch.index}")
    private String esIndex;

    /**
     * The port number of elasticsearch server. It will be automatically read from application.properties
     */
    @Value("${elasticsearch.port}")
    private int esPort;

    /**
     * The name of field to be mapped as date. It will be automatically read from application.properties
     */
    @Value("${elasticsearch.timestampName}")
    private String esTimestampName;


    /**
     * this bean creates a instance of prebuilt transportclient of elasticsearch API with default setting.
     * It tries to connect the elasticsearch and create the index and the mapping when necessary.
     * @return a instance TransportClient
     */
    @Bean
    public TransportClient transportClient()
    {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        logger.debug("add elasticsearch server at " + esHost + ":" + esPort);
        try {
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort));
            createAndMapIndex(client);
        } catch (UnknownHostException e) {
            logger.error("could not connect to node at {}:{}", this.esHost, this.esPort);
            return client;
        }
        logger.info("the connection with elasticsearch server is successfully established");
        return client;
    }


    private void createAndMapIndex(TransportClient client)
    {
        SensorRecord sensorRecord = new SensorBuilder().setType(SensorType.Temperature).setValue(20).build();
        logger.info("try creating index: [" + this. esIndex + "]");
        try {
            client.admin().indices().prepareCreate(this.esIndex)
                    .addMapping(sensorRecord.getClass().getSimpleName(), "{\n" +
                            "    \"" + sensorRecord.getClass().getSimpleName() + "\": {\n" +
                            "      \"properties\": {\n" +
                            "        \"" + this.esTimestampName + "\": {\n" +
                            "          \"type\": \"date\"\n" +
                            "        }\n" +
                            "      }\n" +
                            "    }\n" +
                            "  }")
                    .get();
        } catch (ResourceAlreadyExistsException e) {
            logger.warn("index: [" + this.esIndex + "] already exist, not created");
            return;
        }
        logger.info("index: [" + this.esIndex + "] created.");

    }
}
