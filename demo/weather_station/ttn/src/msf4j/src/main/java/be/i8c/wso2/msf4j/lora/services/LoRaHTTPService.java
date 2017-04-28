package be.i8c.wso2.msf4j.lora.services;

import be.i8c.wso2.msf4j.lora.models.DownlinkRequest;
import be.i8c.wso2.msf4j.lora.services.exceptions.ClientNotRunningException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by yanglin on 27/04/17.
 */

@Service
@Profile("http")
public class LoRaHTTPService extends AbstractLoRaService
{
    private static final Logger logger = LogManager.getLogger(LoRaHTTPService.class);

    private boolean isRunning;

    public LoRaHTTPService()
    {

    }

    @PostConstruct
    private void init()
    {
        isRunning = true;
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
    public void sendDownlink(DownlinkRequest request) throws ClientNotRunningException {
        if (!isRunning)
            throw new ClientNotRunningException();
    }

    @Override
    protected void log(String log, Level level) {
        logger.log(level,log);
    }
}
