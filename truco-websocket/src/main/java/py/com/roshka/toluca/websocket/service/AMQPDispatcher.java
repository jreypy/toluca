package py.com.roshka.toluca.websocket.service;

import py.com.roshka.truco.api.TrucoEvent;

public interface AMQPDispatcher {
    public void send(String topic, String routingKey, String eventName, TrucoEvent trucoEvent);
}
