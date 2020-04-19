package py.com.roshka.toluca.robot;

import com.fasterxml.jackson.databind.ObjectMapper;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.client.communication.TrucoClient;
import py.com.roshka.truco.client.communication.TrucoClientHandler;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;
import py.com.roshka.truco.client.communication.impl.TrucoClientImpl;

import java.util.Map;

public abstract class TolucaBot implements Runnable {
    public static String MAIN_ROOM_ID = "1";
    public static TrucoRoom MAIN_ROOM = new TrucoRoom();

    TrucoClient trucoClient = null;
    ObjectMapper objectMapper = new ObjectMapper();

    public TolucaBot() {

    }

    protected void executeCommand(String commandName, Object message) {
        try {
            trucoClient.send(commandName, message);
        } catch (TrucoClientException e) {
            e.printStackTrace();
        }
    }

    abstract String getName();

    abstract void login();

    abstract void receiveEvent(Map e);

    public void run() {
        trucoClient = new TrucoClientImpl("http://localhost:8091", "ws://localhost:8050");
        try {
            trucoClient.login(getName(), getName(), new TrucoClientHandler() {
                @Override
                public void loginFailed() {

                }

                @Override
                public void afterLogin(TrucoPrincipal trucoPrincipal) throws TrucoClientException {
                    trucoClient.connect();
                }

                @Override
                public void receiveMessage(Object object) {
                    receiveEvent(objectMapper.convertValue(object, Map.class));
                }

                @Override
                public void ready() {
                    login();
                }
            });
        } catch (TrucoClientException e) {
            e.printStackTrace();
        }

    }
}
