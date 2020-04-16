package py.com.roshka.toluca.websocket.handler;

import py.com.roshka.truco.api.TrucoPrincipal;

public class WebSocketPrincipal extends TrucoPrincipal {
    private WebSocketSessionHandler handler;

    public WebSocketPrincipal(String username, String authKey) {
        super(username, authKey);
    }

    public void setHandler(WebSocketSessionHandler handler) {
        this.handler = handler;
    }

    public WebSocketSessionHandler getHandler() {
        return handler;
    }
}
