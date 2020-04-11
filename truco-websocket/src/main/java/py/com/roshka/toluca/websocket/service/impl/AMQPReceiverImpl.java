package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.service.AMQPReceiver;
import py.com.roshka.toluca.websocket.service.TrucoRoomListener;
import py.com.roshka.truco.api.*;

@Component
public class AMQPReceiverImpl implements AMQPReceiver {
    Logger logger = LoggerFactory.getLogger(AMQPReceiverImpl.class);


    ObjectMapper objectMapper;
    TrucoRoomListener trucoRoomListener;

    public AMQPReceiverImpl(ObjectMapper objectMapper, TrucoRoomListener trucoRoomListener) {
        this.objectMapper = objectMapper;
        this.trucoRoomListener = trucoRoomListener;
    }

    @RabbitListener(
            queues = "truco_client"
    )
    void trucoServerEvent(final RabbitResponse rabbitResponse) {
        logger.debug("Receiving Truco Server Event [" + rabbitResponse + "]");
        if (Event.ROOM_CREATED.equalsIgnoreCase(rabbitResponse.getEventName())) {
            trucoRoomListener.roomCreated(objectMapper.convertValue(rabbitResponse.getData(), TrucoRoom.class));
        }

    }

    @RabbitListener(
            queues = "truco_room_client"
    )
    void trucoRoomEvent(final @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, final RabbitResponse rabbitResponse) {
        logger.debug("Receiving Truco Room Event [" + routingKey + "][" + rabbitResponse + "]");

        if (Event.TABLE_POSITION_SETTED.equalsIgnoreCase(rabbitResponse.getEventName())) {
            logger.debug("Table Position Setted [" + routingKey + "]");
            trucoRoomListener.trucoRoomTableEventReceived(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomTableEvent.class));
        } else if (Event.ROOM_TABLE_USER_JOINED.equalsIgnoreCase(rabbitResponse.getEventName())) {
            logger.debug("User joined to Room Table [" + routingKey + "]");
            trucoRoomListener.trucoRoomTableEventReceived(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomTableEvent.class));
        } else if (Event.ROOM_USER_JOINED.equalsIgnoreCase(rabbitResponse.getEventName())) {
            logger.debug("User joined to Room [" + routingKey + "]");
            trucoRoomListener.joinedToRoom(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomEvent.class));
        } else if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase(rabbitResponse.getEventName())) {
            logger.debug("User Table Created in Room [" + routingKey + "]");
            trucoRoomListener.roomTableCreated(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomTable.class));
        } else if (Event.ROOM_USER_LEFT.equalsIgnoreCase(rabbitResponse.getEventName())) {
            logger.debug("User joined to Room [" + routingKey + "]");
            trucoRoomListener.userLeftTheRoom(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomEvent.class));
        }

    }
}
