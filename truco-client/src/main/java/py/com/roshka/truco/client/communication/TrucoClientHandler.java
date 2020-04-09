package py.com.roshka.truco.client.communication;

import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;

public interface TrucoClientHandler {
    void loginFailed();

    void afterLogin(TrucoPrincipal trucoPrincipal) throws TrucoClientException;

    void receiveMessage(Object object);

    void ready();

}
