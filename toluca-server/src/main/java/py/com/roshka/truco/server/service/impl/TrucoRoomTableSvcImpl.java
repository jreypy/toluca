package py.com.roshka.truco.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.Player;
import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.api.TrucoGamePlay;
import py.com.roshka.truco.server.beans.holder.TrucoRoomHolder;
import py.com.roshka.truco.server.service.TrucoRoomTableSvc;
import py.com.roshka.truco.server.service.TrucoUserService;

@Component
public class TrucoRoomTableSvcImpl implements TrucoRoomTableSvc {
    static String TRUCO_ROOM_EVENT = "truco_room_event";

    TrucoUserService trucoUserService;
    TrucoRoomSvcImpl trucoRoomSvc;
    RabbitTemplate rabbitTemplate;
    ObjectMapper objectMapper;

    public TrucoRoomTableSvcImpl(TrucoUserService trucoUserService, TrucoRoomSvcImpl trucoRoomSvc, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.trucoUserService = trucoUserService;
        this.trucoRoomSvc = trucoRoomSvc;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public TrucoGameEvent startGame(String roomId, String tableId) {
        TrucoRoomHolder trucoRoomHolder = ((TrucoRoomSvcImpl) trucoRoomSvc).getTrucoRoomHolder(roomId);
        trucoRoomHolder.getTrucoTableHolder(tableId).getTrucoGameHolder().startGame();

        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setEventName(Event.TRUCO_GAME_REQUEST);
        trucoGameEvent.setMessage("Starting Truco Game [" + tableId + "]");

        //rabbitTemplate.convertAndSend(TRUCO_ROOM_EVENT, roomId, new RabbitResponse(Event.TRUCO_GAME_REQUEST, trucoGameEvent.getClass().getCanonicalName(), objectMapper.convertValue(trucoGameEvent, HashMap.class)));

        return trucoGameEvent;
    }

    @Override
    public TrucoGameEvent play(String roomId, String tableId, TrucoGamePlay trucoGamePlay) {
        TrucoRoomHolder trucoRoomHolder = trucoRoomSvc.getTrucoRoomHolder(roomId);
        trucoGamePlay.setPlayer(new Player(trucoUserService.getTrucoUser().getId(), trucoUserService.getTrucoUser().getUsername()));
        trucoRoomHolder.getTrucoTableHolder(tableId).getTrucoGameHolder().play(trucoGamePlay);
        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setEventName(Event.PLAY_RESPONSE);
        trucoGameEvent.setMessage("Playing [" + tableId + "]");
        return trucoGameEvent;
    }
}
