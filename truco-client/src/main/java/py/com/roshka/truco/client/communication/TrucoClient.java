package py.com.roshka.truco.client.communication;

import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;

import java.net.MalformedURLException;

public interface TrucoClient {
    public TrucoPrincipal login(String username, String password) throws MalformedURLException, TrucoClientException;
}
