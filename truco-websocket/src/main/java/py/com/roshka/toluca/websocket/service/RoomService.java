package py.com.roshka.toluca.websocket.service;

import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.com.roshka.truco.api.TrucoRoomTable;

import java.util.List;

public interface RoomService {

    TrucoRoom createRoom(TrucoRoom trucoRoom);

    TrucoRoomEvent joinRoom(TrucoRoom trucoRoom);

    TrucoRoom findRoomById(String id);

    List<TrucoRoom> findAllRooms();

    TrucoRoomTable createRoomTable(TrucoRoomTable trucoRoomTable);

}
