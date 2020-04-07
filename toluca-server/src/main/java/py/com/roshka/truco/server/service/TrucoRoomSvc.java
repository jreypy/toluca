package py.com.roshka.truco.server.service;

import py.com.roshka.truco.api.Chair;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomTable;

import java.util.List;

public interface TrucoRoomSvc {

    List<TrucoRoom> findAllRooms();

    TrucoRoom create(TrucoRoom trucoRoom);

    TrucoRoom delete(TrucoRoom trucoRoom);

    TrucoRoomTable addTable(String roomId, TrucoRoomTable trucoRoomTable);

    TrucoRoomTable deleteTable(String roomId, String tableId);

    void addChair(String tableId, Chair chair);

    void removeChair(String tableId, Integer chairPosition);


}
