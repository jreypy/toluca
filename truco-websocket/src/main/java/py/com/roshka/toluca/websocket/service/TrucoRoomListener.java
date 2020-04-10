package py.com.roshka.toluca.websocket.service;

import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomEvent;

public interface TrucoRoomListener {

    public void roomCreated(TrucoRoom trucoRoom);

    public void joinedToRoom(String roomId, TrucoRoomEvent trucoRoomEvent);

    public void userLeftTheRoom(String roomId, TrucoRoomEvent trucoRoomEvent);


}
