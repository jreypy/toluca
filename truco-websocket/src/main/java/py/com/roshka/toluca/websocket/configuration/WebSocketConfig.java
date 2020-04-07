package py.com.roshka.toluca.websocket.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import py.com.roshka.toluca.websocket.handler.RoomHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  RoomHandler roomHandler;

  public WebSocketConfig(RoomHandler roomHandler) {
    this.roomHandler = roomHandler;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
    webSocketHandlerRegistry.addHandler(roomHandler, "/ws").setAllowedOrigins("*");
  }
}