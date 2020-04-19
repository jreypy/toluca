package py.edu.uca.fcyt.net;

import py.edu.uca.fcyt.toluca.RoomClient;

import java.io.IOException;

public class CommunicatorProvider {

    // Default behaviour
    public static Class intanceType = CommunicatorClientImpl.class;
    static CommunicatorClient instance = null;


    static public CommunicatorClient getInstance(RoomClient client, String serverString, Integer portNumber) throws IOException {
        if (instance != null)
            return instance;

        if (intanceType == null)
            throw new IllegalArgumentException("CommunicatorProvider not configured");

        try {
            instance = (CommunicatorClient) intanceType.getConstructor(RoomClient.class, String.class, Integer.class).newInstance(client, serverString, portNumber);
            return instance;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating Communicator client [" + intanceType + "]", e);
        }
    }

    static public CommunicatorClient getInstance() {
        return instance;
    }

    static public void setInstance(CommunicatorClient communicatorClient) {
        CommunicatorProvider.instance = communicatorClient;
    }
}
