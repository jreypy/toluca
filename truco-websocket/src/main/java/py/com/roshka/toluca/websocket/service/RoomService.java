package py.com.roshka.toluca.websocket.service;

import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomEvent;

import java.util.List;

public interface RoomService {

    void connect(String user);

    TrucoRoom createRoom(TrucoRoom trucoRoom);

    TrucoRoomEvent joinRoom(TrucoRoom trucoRoom);

    TrucoRoom findRoomById(String id);

    List<TrucoRoom> findAllRooms();


}
