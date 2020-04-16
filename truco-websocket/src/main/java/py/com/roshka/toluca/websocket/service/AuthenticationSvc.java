package py.com.roshka.toluca.websocket.service;

import org.springframework.web.socket.WebSocketSession;
import py.com.roshka.truco.api.TrucoPrincipal;

public interface AuthenticationSvc {
    TrucoPrincipal getTrucoPrincipal(WebSocketSession session);
}
