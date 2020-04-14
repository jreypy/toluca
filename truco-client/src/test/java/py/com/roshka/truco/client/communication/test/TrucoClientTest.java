package py.com.roshka.truco.client.communication.test;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.junit.jupiter.api.Test;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.client.communication.TrucoClient;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;
import py.com.roshka.truco.client.communication.impl.TrucoClientImpl;

import java.net.MalformedURLException;

public class TrucoClientTest {
    static Logger logger = Logger.getLogger(TrucoClientTest.class);

    static {
        BasicConfigurator.configure();
    }

    @Test
    public void login() throws MalformedURLException, TrucoClientException {
        TrucoClient trucoClient = new TrucoClientImpl("http://ec2-184-73-89-227.compute-1.amazonaws.com:8091", "http://ec2-184-73-89-227.compute-1.amazonaws.com:8050");
        TrucoPrincipal trucoPrincipal = trucoClient.login("prueba", "prueba", null);
        logger.debug("TrucoPrincipal token [" + trucoPrincipal.getAuthKey() + "]");

    }

    @Test
    public void connectToWs() throws TrucoClientException {
        TrucoClient trucoClient = new TrucoClientImpl("http://localhost:8091", "ws://localhost:8050");
        TrucoPrincipal trucoPrincipal = trucoClient.login("prueba", "prueba", null);
        ((TrucoClientImpl) trucoClient).connect();
        logger.debug("TrucoClient was connected to Websocket");
        // wscat -c ws://ec2-184-73-89-227.compute-1.amazonaws.com:8050/ws
    }

    public static void main(String[] args) {

    }
}
