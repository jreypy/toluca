package py.com.roshka.toluca.websocket.service;

import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomEvent;

import java.util.List;

public interface RoomService {

    public void connect(String user);

    public TrucoRoom createRoom(TrucoRoom trucoRoom);

    public TrucoRoomEvent joinRoom(TrucoRoom trucoRoom);

    List<TrucoRoom> findAllRooms();


}
