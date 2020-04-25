package py.com.roshka.toluca.websocket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.service.ChannelSvc;

import java.util.HashSet;
import java.util.Set;

public class ChannelHolder implements ChannelSvc {
    Logger logger = LoggerFactory.getLogger(ChannelHolder.class);
    final String name;
    final WebSocketSessionManager webSocketSessionManager;
    Set<WebSocketSessionHandler> handlers = new HashSet<>();

    public ChannelHolder(String name, WebSocketSessionManager webSocketSessionManager) {
        this.name = name;
        this.webSocketSessionManager = webSocketSessionManager;
    }


    @Override
    public void addToChannel(WebSocketSessionHandler handler) {
        handlers.add(handler);
        logger.info("User [" + handler.getUsername() + "] added to the channel [" + name + "][" + handlers.size() + "]");
    }

    @Override
    public void addToChannel(String channel, String username) {
        throw new IllegalStateException("Error!!! [addToChannel]");
    }

    @Override
    public void sendEvent(Event event) {
        logger.debug("Send event Type [" + event.getType() + "] to channel [" + name + "]");
        // send event
        for (WebSocketSessionHandler handler : handlers) {
            webSocketSessionManager.sendEvent(handler, event);
        }
    }

    @Override
    public void sendChannelEvent(String channel, Event event) {
        throw new IllegalStateException("Error!!! [sendChannelEvent]");
    }

    @Override
    public void sendDirectMessage(String username, Event event) {
        for (WebSocketSessionHandler handler : handlers) {
            if (handler.getUsername().equalsIgnoreCase(username)){
                webSocketSessionManager.sendEvent(handler, event);
            }
        }
    }
}
