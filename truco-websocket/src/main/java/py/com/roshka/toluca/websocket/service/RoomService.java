package py.com.roshka.toluca.websocket.service;

import py.com.roshka.truco.api.TrucoRoom;

import java.util.List;

public interface RoomService {

    public void connect(String user);

    public TrucoRoom createRoom(TrucoRoom trucoRoom);

    List<TrucoRoom> findAllRooms();


}
