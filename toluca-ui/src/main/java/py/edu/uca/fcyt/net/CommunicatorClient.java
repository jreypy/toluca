package py.edu.uca.fcyt.net;

import py.edu.uca.fcyt.toluca.net.Communicator;
import py.edu.uca.fcyt.toluca.net.EventDispatcher;

public abstract class CommunicatorClient extends Communicator {
    public CommunicatorClient(EventDispatcher eventDispatcher) {
        super(eventDispatcher);
    }

    public CommunicatorClient() {
    }

    abstract public void setLoggedIn(boolean loggedIn);
}
