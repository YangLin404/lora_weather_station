package be.i8c.wso2.msf4j.lora.config;

import be.i8c.wso2.msf4j.lora.models.common.Device;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanglin on 12/05/17.
 */
@Configuration
@Profile("proximus")
public class AppConfigProximus {

    private static final Logger logger = LogManager.getLogger(AppConfigProximus.class);

    /**
     * list of deviceIds of the devices which we are expecting, loaded from application.properties
     */
    @Value("#{'${proximus.device.deviceid}'.split(';')}")
    private List<String> deviceIds;

    /**
     * this bean creates a list of devices defined in application.properties.
     * @return a Map with deviceId as key and instance of Device as Value.
     */
    @Bean
    public Map<String,Device> devices()
    {
        Map<String,Device> devices = new HashMap<>();
        deviceIds.forEach(id -> {
                Device device = new Device(id,null);
                devices.put(id,device);
        });
        return devices;
    }
}
