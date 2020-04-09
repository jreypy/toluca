package py.edu.uca.fcyt.net;

import py.edu.uca.fcyt.toluca.RoomClient;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class CommunicatorProvider {

    // Default behaviour
    static Class intanceType = CommunicatorClient.class;


    static public CommunicatorClient getIntance(RoomClient client, String serverString, Integer portNumber) throws IOException {
        if (intanceType == null)
            throw new IllegalArgumentException("CommunicatorProvider not configured");
        try {
            return (CommunicatorClient) intanceType.getConstructor(RoomClient.class, String.class, Integer.class).newInstance(client, serverString, portNumber);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating Communicator client [" + intanceType + "]");
        }
    }
}
