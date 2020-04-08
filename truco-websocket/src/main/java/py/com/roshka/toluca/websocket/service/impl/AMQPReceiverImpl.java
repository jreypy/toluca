package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.service.AMQPReceiver;
import py.com.roshka.toluca.websocket.service.TrucoRoomListener;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.RabbitRequest;
import py.com.roshka.truco.api.RabbitResponse;
import py.com.roshka.truco.api.TrucoRoom;

@Component
public class AMQPReceiverImpl implements AMQPReceiver {
    Logger logger = LoggerFactory.getLogger(AMQPReceiverImpl.class);


    ObjectMapper objectMapper;
    TrucoRoomListener trucoRoomListener;

    public AMQPReceiverImpl(ObjectMapper objectMapper, TrucoRoomListener trucoRoomListener) {
        this.objectMapper = objectMapper;
        this.trucoRoomListener = trucoRoomListener;
    }

    @RabbitListener(queues = "truco_client")
    void trucoServerEvent(final RabbitResponse rabbitResponse) {
        logger.debug("Receiving [" + rabbitResponse + "]");
        if (Event.ROOM_CREATED.equalsIgnoreCase(rabbitResponse.getEventName())) {
            trucoRoomListener.roomCreated(objectMapper.convertValue(rabbitResponse.getData(), TrucoRoom.class));
        }

    }
}
