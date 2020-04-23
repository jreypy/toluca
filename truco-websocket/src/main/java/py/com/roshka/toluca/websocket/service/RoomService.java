package py.com.roshka.toluca.websocket.service;

import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.request.*;

import java.util.Map;

public interface RoomService {

    Map createRoom(TrucoRoom trucoRoom);

    Map joinRoom(RoomRequest trucoRoom);

    Map findRoomById(String id);

    Map findAllRooms();

    Map createRoomTable(TrucoRoomTable trucoRoomTable);

    Map setTablePosition(TablePositionRequest tablePositionRequest);

    Map joinRoomTable(JoinRoomTableRequest trucoRoomTable);

    Map startGame(StartGameRequest startGameRequest);

    Map startHand(StartHandRequest startHandquest);

    Map play(TrucoGamePlay trucoGamePlay);
}
