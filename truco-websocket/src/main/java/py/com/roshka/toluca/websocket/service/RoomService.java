package py.com.roshka.toluca.websocket.service;

import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.request.JoinRoomTableRequest;
import py.com.roshka.truco.api.request.RoomRequest;
import py.com.roshka.truco.api.request.StartGameRequest;
import py.com.roshka.truco.api.request.TablePositionRequest;

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

    Map play(TrucoGamePlay trucoGamePlay);
}
