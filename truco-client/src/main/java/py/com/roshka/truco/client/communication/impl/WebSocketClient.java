package py.com.roshka.truco.client.communication.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    Logger logger = LoggerFactory.getLogger(WebSocketClient.class);

    ObjectMapper objectMapper;
    WebSocketClientListener webSocketClientListener;
    private boolean finished = false;
    private Map<String, Object> attributes = new LinkedHashMap<>();


    public WebSocketClient(URI serverURI, ObjectMapper objectMapper, WebSocketClientListener listener, Map  headers) {
        super(serverURI, headers);
        this.objectMapper = objectMapper;
        webSocketClientListener = listener;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        logger.debug("opened connection");
        webSocketClientListener.onOpen(this);
        // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
    }

    @Override
    public void onMessage(String message) {
        ObjectMapper o = new ObjectMapper();
        try {
            Map map = o.readValue(message, Map.class);
            this.finished = this.finished || webSocketClientListener.onMessage(this, map);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        logger.debug("*****************************");
        logger.debug("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason + "(" + finished + ")");
        webSocketClientListener.onClose(this);
    }

    @Override
    public void onError(Exception ex) {
        logger.error(ex.getMessage(), ex);
        webSocketClientListener.onError(ex);
        // if the error is fatal then onClose will be called additionally
    }

    public boolean isFinished() {
        return finished;
    }

    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }
}