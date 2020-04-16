package py.com.roshka.toluca.websocket.service;

import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.handler.WebSocketSessionHandler;

import java.io.IOException;

public interface ChannelSvc {

    void addToChannel(WebSocketSessionHandler handler);

    void addToChannel(String channel, String username);

    void sendEvent(Event event);

    void sendChannelEvent(String channel, Event event);

    void sendDirectMessage(String username, Event event);


}
