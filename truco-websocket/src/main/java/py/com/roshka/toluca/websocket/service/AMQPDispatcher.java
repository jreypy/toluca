package py.com.roshka.toluca.websocket.service;

public interface AMQPDispatcher {
    public void send(String topic, String routingKey, Object data);
}
