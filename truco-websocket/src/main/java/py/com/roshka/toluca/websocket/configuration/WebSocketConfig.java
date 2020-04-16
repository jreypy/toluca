package py.com.roshka.toluca.websocket.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import py.com.roshka.toluca.websocket.handler.WebSocketSessionManager;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {



    final WebSocketSessionManager webSocketSessionManager;

    public WebSocketConfig(WebSocketSessionManager webSocketSessionManager) {
        this.webSocketSessionManager = webSocketSessionManager;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(webSocketSessionManager, "/ws").setAllowedOrigins("*");
    }


}