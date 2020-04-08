package py.com.roshka.toluca.websocket.service.impl;

import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.service.AMQPDispatcher;
import py.com.roshka.toluca.websocket.service.RoomService;


@Component
public class WSRoomServiceImpl implements RoomService {


    AMQPDispatcher amqpDispatcher;

    public WSRoomServiceImpl(AMQPDispatcher amqpDispatcher) {
        this.amqpDispatcher = amqpDispatcher;
    }

    @Override
    public void connect(String user) {
        amqpDispatcher.send("truco", "room", user);
    }
}
