package py.com.roshka.truco.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.server.beans.holder.TrucoRoomHolder;
import py.com.roshka.truco.server.beans.holder.TrucoTableHolder;
import py.com.roshka.truco.server.service.AMQPSender;
import py.com.roshka.truco.server.service.TrucoRoomTableSvc;
import py.com.roshka.truco.server.service.TrucoUserService;

@Component
public class TrucoRoomTableSvcImpl implements TrucoRoomTableSvc {
    static String TRUCO_ROOM_EVENT = "truco_room_event";

    TrucoUserService trucoUserService;
    TrucoRoomSvcImpl trucoRoomSvc;
    // RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper;
    final AMQPSender amqpSender;

    public TrucoRoomTableSvcImpl(TrucoUserService trucoUserService, TrucoRoomSvcImpl trucoRoomSvc, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, AMQPSender amqpSender) {
        this.trucoUserService = trucoUserService;
        this.trucoRoomSvc = trucoRoomSvc;
        this.objectMapper = objectMapper;
        this.amqpSender = amqpSender;
    }

    @Override
    public TrucoGameEvent startGame(String roomId, String tableId) {
        TrucoRoomHolder trucoRoomHolder = trucoRoomSvc.getTrucoRoomHolder(roomId);
        TrucoTableHolder trucoTableHolder = trucoRoomHolder.getTrucoTableHolder(tableId);
        trucoTableHolder.startGame();

        if (TrucoRoomTable.IN_PROGRESS.equalsIgnoreCase(trucoTableHolder.getStatus())){
            // Send Event
            TrucoRoomTableEvent trucoRoomTableEvent = new TrucoRoomTableEvent();
            trucoRoomTableEvent.setEventName(Event.ROOM_TABLE_STATUS_UPDATED);
            trucoRoomTableEvent.setMessage("Room Table Status [" + tableId + "] updated [" + trucoTableHolder.getStatus() + "]");
            trucoRoomTableEvent.setTableId(tableId);

            TrucoRoomTableDescriptor tableDescriptor = trucoTableHolder.descriptor();
            tableDescriptor.setPositions(trucoTableHolder.getPositions());
            trucoRoomTableEvent.setTable(tableDescriptor);

            amqpSender.convertAndSend(AMQPSenderImpl.CHANNEL_ROOM_ID + roomId, trucoRoomTableEvent);
        }

        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setEventName(Event.GAME_STARTED);
        trucoGameEvent.setMessage("Starting Truco Game [" + tableId + "]");

        //rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, roomId, new RabbitResponse(Event.TRUCO_GAME_REQUEST, trucoGameEvent.getClass().getCanonicalName(), objectMapper.convertValue(trucoGameEvent, HashMap.class)));

        return trucoGameEvent;
    }

    @Override
    public TrucoGameEvent startHand(String roomId, String tableId) {
        TrucoRoomHolder trucoRoomHolder = trucoRoomSvc.getTrucoRoomHolder(roomId);
        trucoRoomHolder.getTrucoTableHolder(tableId).getTrucoGameHolder().startHand(trucoUserService.getTrucoUser());

        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setEventName(Event.HAND_STARTED);
        trucoGameEvent.setMessage("Starting Truco Game [" + tableId + "]");

        return trucoGameEvent;
    }


    @Override
    public TrucoGameEvent play(String roomId, String tableId, TrucoGamePlay trucoGamePlay) {
        TrucoRoomHolder trucoRoomHolder = trucoRoomSvc.getTrucoRoomHolder(roomId);
        trucoGamePlay.setPlayer(new Player(trucoUserService.getTrucoUser().getId(), trucoUserService.getTrucoUser().getUsername()));
        trucoRoomHolder.getTrucoTableHolder(tableId).play(trucoGamePlay);
        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setEventName(Event.PLAY_RESPONSE);
        trucoGameEvent.setMessage("Playing [" + tableId + "]");
        return trucoGameEvent;
    }
}
