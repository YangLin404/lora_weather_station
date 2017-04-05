package be.i8c.wso2.msf4j.lora.Services;

import be.i8c.wso2.msf4j.lora.repositories.LoRaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.common.messages.UplinkMessage;
import org.thethingsnetwork.data.mqtt.Client;

import javax.annotation.PostConstruct;
import javax.ws.rs.Path;
import java.net.URISyntaxException;
import java.util.Arrays;

/**
 * Created by yanglin on 3/04/17.
 */
@Component
@Profile("ttn")
@Path("api/ttn")
public class LoRaRestServiceTTN
{
    private static final Logger LOGGER = LogManager.getLogger(LoRaRestServiceTTN.class);


    @Value("${ttn.region}")
    private String region;

    @Value("${ttn.appId}")
    private String appId;

    @Value("${ttn.accessKey}")
    private String accessKey;

    //@Autowired
    //private LoRaRepository repo;

    public LoRaRestServiceTTN()
    {

    }

    @PostConstruct
    public void init()
    {
        Client client;
        try {
            client = new Client(region, appId, accessKey);
            client.onError((Throwable _error) -> LOGGER.error("connect error"));
            client.onConnected((Connection _client) -> LOGGER.info("connected"));
            client.onMessage((String devId, DataMessage data) -> LOGGER.info("Message: " + devId + " " + Arrays.toString(((UplinkMessage) data).getPayloadRaw())));
            client.start();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
