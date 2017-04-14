package be.i8c.wso2.msf4j.lora;

import be.i8c.wso2.msf4j.lora.services.LoRaTTNService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;


import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by yanglin on 13/04/17.
 */

public class LoRaTTNControllerTest {
    @Mock
    LoRaTTNService service;
    @InjectMocks
    LoRaTTNController controller;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void startClient_ClientStartSuccessfully_ShouldReturnAccepted() throws Exception
    {
        doNothing().when(service).startClient();
        Response excepted = Response.accepted().build();

        Response actual = controller.startClient();
        assertEquals(excepted.getStatus(), actual.getStatus());
        verify(service, times(1)).startClient();
    }

    @Test
    public void stopClient_ClientStopSuccessfully_ShouldReturnAccepted() throws Exception {
        doNothing().when(service).stopClient();
        Response excepted = Response.accepted().build();

        Response actual = controller.stopClient();

        assertEquals(excepted.getStatus(), actual.getStatus());
        verify(service, times(1)).stopClient();
    }



}