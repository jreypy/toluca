package py.com.roshka.toluca.websocket.handler;

import org.springframework.web.socket.WebSocketSession;
import py.com.roshka.truco.api.TrucoPrincipal;

public class WebsocketHelper {

    static public WebSocketPrincipal getTrucoPrincipal(WebSocketSession session) {
//        String auth = session.getHandshakeHeaders().get("Authentication").get(0);
        String auth = session.getUri().getQuery();
        return new WebSocketPrincipal(auth.split("-")[0], auth);
    }


}
