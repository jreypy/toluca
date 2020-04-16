package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.service.AMQPReceiver;
import py.com.roshka.toluca.websocket.service.ChannelSvc;
import py.com.roshka.truco.api.JoinRabbitResponse;
import py.com.roshka.truco.api.RabbitResponse;

@Component
public class AMQPReceiverImpl implements AMQPReceiver {
    Logger logger = LoggerFactory.getLogger(AMQPReceiverImpl.class);


    ObjectMapper objectMapper;
    ChannelSvc channelSvc;

    public AMQPReceiverImpl(ObjectMapper objectMapper, ChannelSvc channelSvc) {
        this.objectMapper = objectMapper;
        this.channelSvc = channelSvc;
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
    void trucoRoomEvent(final @Header(AmqpHeaders.RECEIVED_EXCHANGE) String topic, final @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, final RabbitResponse rabbitResponse) {
        logger.debug("Receiving Truco Room All Event [" + topic + "][" + routingKey + "][" + rabbitResponse + "]");

        channelSvc.sendEvent(new Event(rabbitResponse.getEventName(), rabbitResponse.getData()));

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
    void trucoRoomIdEvent(final @Header(AmqpHeaders.RECEIVED_EXCHANGE) String topic, final @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, final RabbitResponse rabbitResponse) {
        logger.debug("Receiving Truco Room ID Event [" + topic + "][" + routingKey + "][" + rabbitResponse + "]");
        channelSvc.sendChannelEvent(rabbitResponse.getChannel(), new Event(rabbitResponse.getEventName(), rabbitResponse.getData()));

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
    void trucoRoomJoinEvent(final @Header(AmqpHeaders.RECEIVED_EXCHANGE) String topic, final @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, final JoinRabbitResponse joinRabbitResponse) {
        logger.debug("Receiving Truco Room Join Event [" + topic + "][" + routingKey + "][" + joinRabbitResponse + "]");
        channelSvc.addToChannel(joinRabbitResponse.getChannel(), joinRabbitResponse.getTrucoUser().getUsername());
        channelSvc.sendChannelEvent(joinRabbitResponse.getChannel(), new Event(joinRabbitResponse.getRabbitResponse().getEventName(), joinRabbitResponse.getRabbitResponse().getData()));
    }

    @RabbitListener(
            queues = "truco_table_client"
    )
    void trucoTableEvent(final @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, final RabbitResponse rabbitResponse) {
        logger.debug("Receiving Truco Game Event [" + routingKey + "][" + rabbitResponse + "]");
        // TODO Send to Table
        channelSvc.sendChannelEvent(rabbitResponse.getChannel(), new Event(rabbitResponse.getEventName(), rabbitResponse.getData()));
    }


    @RabbitListener(
            queues = "direct_message_queue"
    )
    void directMessageEvent(final @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey, final RabbitResponse rabbitResponse) {
        logger.debug("Receiving DirectMessage Event [ " + routingKey + "]");
        channelSvc.sendDirectMessage(routingKey, new Event(rabbitResponse.getEventName(), rabbitResponse.getData()));
    }
}
