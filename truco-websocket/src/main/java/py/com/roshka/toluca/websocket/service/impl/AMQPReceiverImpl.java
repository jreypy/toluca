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
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.handler.RoomHandler;
import py.com.roshka.toluca.websocket.service.AMQPReceiver;
import py.com.roshka.toluca.websocket.service.TrucoRoomListener;
import py.com.roshka.truco.api.*;

@Component
public class AMQPReceiverImpl implements AMQPReceiver {
    Logger logger = LoggerFactory.getLogger(AMQPReceiverImpl.class);


    ObjectMapper objectMapper;
    RoomHandler roomHandler;

    public AMQPReceiverImpl(ObjectMapper objectMapper, RoomHandler roomHandler) {
        this.objectMapper = objectMapper;
        this.roomHandler = roomHandler;
    }


    /**
     * Eventos de Room
     *
     * @param routingKey
     * @param rabbitResponse
     */
    @RabbitListener(
            queues = "truco_room_all_client"
    )
    void trucoRoomEvent(final @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, final RabbitResponse rabbitResponse) {
        logger.debug("Receiving Truco Room All Event [" + routingKey + "][" + rabbitResponse + "]");

        roomHandler.sendEvent(new Event(rabbitResponse.getEventName(), rabbitResponse.getData()));

//        if (Event.TABLE_POSITION_SETTED.equalsIgnoreCase(rabbitResponse.getEventName())) {
//            logger.debug("Table Position Setted [" + routingKey + "]");
//            trucoRoomListener.trucoRoomTableEventReceived(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomTableEvent.class));
//        } else if (Event.ROOM_TABLE_USER_JOINED.equalsIgnoreCase(rabbitResponse.getEventName())) {
//            logger.debug("User joined to Room Table [" + routingKey + "]");
//            trucoRoomListener.trucoRoomTableEventReceived(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomTableEvent.class));
//        } else if (Event.ROOM_USER_JOINED.equalsIgnoreCase(rabbitResponse.getEventName())) {
//            logger.debug("User joined to Room [" + routingKey + "]");
//            trucoRoomListener.joinedToRoom(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomEvent.class));
//        } else if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase(rabbitResponse.getEventName())) {
//            logger.debug("User Table Created in Room [" + routingKey + "]");
//            trucoRoomListener.roomTableCreated(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomTable.class));
//        } else if (Event.ROOM_USER_LEFT.equalsIgnoreCase(rabbitResponse.getEventName())) {
//            logger.debug("User joined to Room [" + routingKey + "]");
//            trucoRoomListener.userLeftTheRoom(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomEvent.class));
//        }

    }

    /**
     * Eventos de Room Especifico
     *
     * @param routingKey
     * @param rabbitResponse
     */
    @RabbitListener(
            queues = "truco_room_id_client"
    )
    void trucoRoomIdEvent(final @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, final RabbitResponse rabbitResponse) {
        logger.debug("Receiving Truco Room ID Event [" + routingKey + "][" + rabbitResponse + "]");
        roomHandler.sendRoomEvent(rabbitResponse.getRoomId(), new Event(rabbitResponse.getEventName(), rabbitResponse.getData()));

//        if (Event.TABLE_POSITION_SETTED.equalsIgnoreCase(rabbitResponse.getEventName())) {
//            logger.debug("Table Position Setted [" + routingKey + "]");
//            trucoRoomListener.trucoRoomTableEventReceived(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomTableEvent.class));
//        } else if (Event.ROOM_TABLE_USER_JOINED.equalsIgnoreCase(rabbitResponse.getEventName())) {
//            logger.debug("User joined to Room Table [" + routingKey + "]");
//            trucoRoomListener.trucoRoomTableEventReceived(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomTableEvent.class));
//        } else if (Event.ROOM_USER_JOINED.equalsIgnoreCase(rabbitResponse.getEventName())) {
//            logger.debug("User joined to Room [" + routingKey + "]");
//            trucoRoomListener.joinedToRoom(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomEvent.class));
//        } else if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase(rabbitResponse.getEventName())) {
//            logger.debug("User Table Created in Room [" + routingKey + "]");
//            trucoRoomListener.roomTableCreated(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomTable.class));
//        } else if (Event.ROOM_USER_LEFT.equalsIgnoreCase(rabbitResponse.getEventName())) {
//            logger.debug("User joined to Room [" + routingKey + "]");
//            trucoRoomListener.userLeftTheRoom(routingKey, objectMapper.convertValue(rabbitResponse.getData(), TrucoRoomEvent.class));
//        }

    }

    /**
     * Eventos de Room Especifico
     *
     * @param routingKey
     */
    @RabbitListener(
            queues = "truco_room_join_client"
    )
    void trucoRoomJoinEvent(final @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, final JoinRabbitResponse joinRabbitResponse) {
        logger.debug("Receiving Truco Room Join Event [" + routingKey + "][" + joinRabbitResponse + "]");
        roomHandler.addToRoom(joinRabbitResponse.getRoomId(), joinRabbitResponse.getTrucoUser().getUsername());
        roomHandler.sendRoomEvent(joinRabbitResponse.getRoomId(), new Event(joinRabbitResponse.getRabbitResponse().getEventName(), joinRabbitResponse.getRabbitResponse().getData()));
    }

    @RabbitListener(
            queues = "truco_table_client"
    )
    void trucoTableEvent(final @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, final RabbitResponse rabbitResponse) {
        logger.debug("Receiving Truco Game Event [" + routingKey + "][" + rabbitResponse + "]");
        // TODO Send to Table
        roomHandler.sendRoomEvent(routingKey, new Event(rabbitResponse.getEventName(), rabbitResponse.getData()));
    }
}
