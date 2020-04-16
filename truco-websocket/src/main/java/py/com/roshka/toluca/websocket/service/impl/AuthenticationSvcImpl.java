package py.com.roshka.toluca.websocket.service.impl;

import org.springframework.web.socket.WebSocketSession;
import py.com.roshka.toluca.websocket.service.AuthenticationSvc;
import py.com.roshka.truco.api.TrucoPrincipal;

public class AuthenticationSvcImpl implements AuthenticationSvc {
    //TODO
    @Override
    public TrucoPrincipal getTrucoPrincipal(WebSocketSession session) {
        return null;
    }
}
