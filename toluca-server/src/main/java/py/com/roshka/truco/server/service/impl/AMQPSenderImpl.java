package py.com.roshka.truco.server.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.server.service.AMQPSender;

@Service
public class AMQPSenderImpl implements AMQPSender {

    static String TRUCO_ROOM_EVENT = "truco_room_event";
    static String TRUCO_GAME_EVENT = "truco_game_event";
    static String DIRECT_MESSAGE_EVENT = "direct_message";

    static String ROOM_ALL_ROUTING_KEY = "room_all";
    static String ROOM_ID_ROUTING_KEY = "room_id";
    static String ROOM_JOIN_ROUTING_KEY = "room_join";
    static String ROOM_LOGOUT_ROUTING_KEY = "room_logout";

    static public String DIRECT_MESSAGE = "user/";
    static public String CHANNEL_PUBLIC = "public";
    static public String CHANNEL_ROOM = "room";
    static public String CHANNEL_ROOM_ID = "room/";
    static public String CHANNEL_TABLE_ID = "/table/";

    final private RabbitTemplate rabbitTemplate;


    public AMQPSenderImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void convertAndSend(TrucoEvent trucoEvent) {
        rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, ROOM_ALL_ROUTING_KEY, new RabbitResponse(Event.TRUCO_ROOM_EVENT, CHANNEL_PUBLIC, trucoEvent));
    }

    @Override
    public void joinToChannel(String channel, TrucoUser trucoUser, TrucoRoomEvent trucoEvent, String joinTo) {
        rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, ROOM_JOIN_ROUTING_KEY, new JoinRabbitResponse(joinTo, trucoUser, new RabbitResponse(Event.TRUCO_ROOM_EVENT, channel, trucoEvent)));
    }

    @Override
    public void convertAndSend(String channel, TrucoRoomEvent trucoEvent) {
        rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, ROOM_ID_ROUTING_KEY, new RabbitResponse(Event.TRUCO_ROOM_EVENT, channel, trucoEvent));
    }

    @Override
    public void convertAndSend(String channel, TrucoRoomTableEvent trucoEvent) {
        rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, ROOM_ID_ROUTING_KEY, new RabbitResponse(Event.TRUCO_TABLE_EVENT, channel, trucoEvent));
    }

    @Override
    public void convertAndSend(TrucoGameEvent trucoGameEvent) {
        rabbitTemplate.convertAndSend(TRUCO_GAME_EVENT, ROOM_ID_ROUTING_KEY, new RabbitResponse(Event.TRUCO_GAME_EVENT,
                CHANNEL_ROOM_ID + trucoGameEvent.getRoomId() + CHANNEL_TABLE_ID + trucoGameEvent.getTableId(),
                trucoGameEvent));
    }

    @Override
    public void convertAndSendDirectMessage(String userId, TrucoGameEvent trucoEvent) {
        rabbitTemplate.convertAndSend(DIRECT_MESSAGE_EVENT, userId, new RabbitResponse(Event.TRUCO_GAME_EVENT,
                DIRECT_MESSAGE + userId,
                trucoEvent));
    }
}
