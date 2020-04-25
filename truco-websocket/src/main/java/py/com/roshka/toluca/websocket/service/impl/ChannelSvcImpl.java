package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.CommandResponse;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.handler.ChannelHolder;
import py.com.roshka.toluca.websocket.handler.WebSocketSessionHandler;
import py.com.roshka.toluca.websocket.handler.WebSocketSessionListener;
import py.com.roshka.toluca.websocket.handler.WebSocketSessionManager;
import py.com.roshka.toluca.websocket.service.AMQPDispatcher;
import py.com.roshka.toluca.websocket.service.ChannelSvc;
import py.com.roshka.toluca.websocket.service.CommandProcessor;
import py.com.roshka.truco.api.TrucoUser;
import py.com.roshka.truco.api.event.LogoutEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChannelSvcImpl implements ChannelSvc, WebSocketSessionListener {
    Logger logger = LoggerFactory.getLogger(ChannelSvcImpl.class);


    static String MAIN_CHANNEL = "main";

    Map<String, ChannelHolder> channels;
    ChannelHolder mainChannel = null;

    final WebSocketSessionManager webSocketSessionManager;
    final AMQPDispatcher amqpDispatcher;
    final CommandProcessor commandProcessor;
    final ObjectMapper objectMapper;


    public ChannelSvcImpl(WebSocketSessionManager webSocketSessionManager, AMQPDispatcher amqpDispatcher, CommandProcessor commandProcessor, ObjectMapper objectMapper) {
        this.webSocketSessionManager = webSocketSessionManager;
        this.amqpDispatcher = amqpDispatcher;
        this.commandProcessor = commandProcessor;
        this.objectMapper = objectMapper;
        channels = new HashMap<>();
        mainChannel = createChannel(MAIN_CHANNEL);
        webSocketSessionManager.addWebSessionSocketListener(this);
    }

    @Override
    public void addToChannel(WebSocketSessionHandler handler) {
        mainChannel.addToChannel(handler);
    }

    @Override
    public void addToChannel(String channel, String username) {
        ChannelHolder channelHolder = getChannel(channel);
        webSocketSessionManager.addToChannel(channelHolder, username);

    }

    @Override
    public void sendEvent(Event event) {
        mainChannel.sendEvent(event);
    }

    @Override
    public void sendChannelEvent(String channel, Event event) {
        getChannel(channel).sendEvent(event);
    }

    @Override
    public void sendDirectMessage(String username, Event event) {
        logger.debug("Send Direct Message to [" + username + "][" + event + "]");
        mainChannel.sendDirectMessage(username, event);
    }

    public ChannelHolder getChannel(String channel) {
        ChannelHolder channelHolder = channels.get(channel);
        if (channels.get(channel) == null) {
            channelHolder = createChannel(channel);
        }
        return channelHolder;
    }

    public synchronized ChannelHolder createChannel(String channel) {
        ChannelHolder channelHolder = channels.get(channel);
        if (channelHolder != null)
            return channelHolder;

        channelHolder = new ChannelHolder(channel, webSocketSessionManager);
        channels.put(channel, channelHolder);
        return channelHolder;
    }

    @Override
    public void messageReceived(String payload) throws IOException {
        CommandResponse commandResponse = commandProcessor.processCommand(objectMapper.readValue(payload, Command.class));
        if (commandResponse != null) {
            webSocketSessionManager.sendCommandResponse(commandResponse);
        }
    }

    @Override
    public void sessionTerminated(WebSocketSessionHandler handler) {
        amqpDispatcher.send("truco_user_event", "logout", py.com.roshka.truco.api.Event.LOGOUT, new LogoutEvent(py.com.roshka.truco.api.Event.LOGOUT, new TrucoUser(handler.getUsername(), handler.getUsername())));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSessionHandler handler) {
        // TODO remove from all channels
        addToChannel(handler);
    }
}
