package be.i8c.wso2.msf4j.lora.services;

import be.i8c.wso2.msf4j.lora.models.DownlinkRequest;
import be.i8c.wso2.msf4j.lora.services.exceptions.ClientNotRunningException;
import be.i8c.wso2.msf4j.lora.services.exceptions.DownlinkException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yanglin on 27/04/17.
 */

@Service
@Profile("http")
public class LoRaHTTPService extends AbstractLoRaService
{
    private static final Logger logger = LogManager.getLogger(LoRaHTTPService.class);

    @Value("${ttn.http.procesId}")
    private String procesId;

    /**
     * the appId, loaded from application.properties
     */
    @Value("${ttn.appId}")
    private String appId;

    /**
     * the accesskey, loaded from application.properties
     */
    @Value("${ttn.accessKey}")
    private String accessKey;

    @Value("${ttn.http.url}")
    private String downlinkUrl;

    private ObjectMapper objectMapper;

    private boolean isRunning;

    public LoRaHTTPService()
    {

    }

    @PostConstruct
    private void init()
    {
        isRunning = true;
        downlinkUrl +=
                appId +
                "/" +
                procesId +
                "?key=" +
                accessKey;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void startClient() throws Exception {
        isRunning = true;
    }

    @Override
    public void stopClient() throws Exception {
        isRunning = false;
    }

    @Override
    public void sendDownlink(DownlinkRequest request) throws RuntimeException {
        if (!isRunning)
            throw new ClientNotRunningException();
        else
        {
            try {
                super.encode(request);
                URL url = new URL(downlinkUrl);
                logger.debug("url is: {}", url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type","application/json");
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());

                String downlinkAsJson = objectMapper.writeValueAsString(request);
                logger.debug("downlink json: {}",downlinkAsJson);
                wr.writeBytes(downlinkAsJson);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                if (!(responseCode == 202))
                    throw new DownlinkException("response from ttn server: " + responseCode);
                logger.debug(responseCode);
                con.disconnect();
            } catch (IOException e ) {
                throw new DownlinkException(e.getMessage());
            }
        }
    }

    @Override
    protected void log(String log, Level level) {
        logger.log(level,log);
    }
}
