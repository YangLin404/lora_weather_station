package be.i8c.wso2.msf4j.lora.config;

import be.i8c.wso2.msf4j.lora.utils.PayloadDecoder;
import be.i8c.wso2.msf4j.lora.utils.PayloadValidator;
import be.i8c.wso2.msf4j.lora.utils.UplinkMessageValidator;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yanglin on 13/04/17.
 */
@Configuration
public class AppConfig
{
    @Value("${decoder.format}")
    private String format;

    @Bean
    public TransportClient transportClient()
    {
        return new PreBuiltTransportClient(Settings.EMPTY);
    }

    @Bean
    public PayloadDecoder payloadDecoder()
    {
        return new PayloadDecoder(format, uplinkMessageValidator());
    }

    @Bean
    public UplinkMessageValidator uplinkMessageValidator()
    {
        return new UplinkMessageValidator();
    }
}
