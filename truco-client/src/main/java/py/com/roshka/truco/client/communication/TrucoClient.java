package py.com.roshka.truco.client.communication;

import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;

import java.net.MalformedURLException;
import java.util.Map;

public interface TrucoClient {
    TrucoPrincipal login(String username, String password, TrucoClientHandler trucoClientHandler) throws TrucoClientException;

    void connect() throws TrucoClientException;

    void send(String commandName, Object commandData) throws TrucoClientException;

    TrucoPrincipal getTrucoPrincipal();
}
