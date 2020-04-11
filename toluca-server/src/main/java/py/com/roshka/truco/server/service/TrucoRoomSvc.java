package py.com.roshka.truco.server.service;

import py.com.roshka.truco.api.*;

import java.util.List;

public interface TrucoRoomSvc {

    List<TrucoRoom> findAllRooms();

    TrucoRoom findRoomById(String roomId);

    TrucoRoom create(TrucoRoom trucoRoom);

    TrucoRoomEvent joinRoom(String roomId, TrucoUser user);

    TrucoRoom delete(TrucoRoom trucoRoom);

    TrucoRoomTable addTable(String roomId, TrucoRoomTable trucoRoomTable);

    TrucoRoomTableEvent joinRoomTable(String roomId, String tableId);

    TrucoRoomTableEvent setTablePosition(String roomId, String tableId, Integer position);

    TrucoRoomTable deleteTable(String roomId, String tableId);

    void addChair(String tableId, Chair chair);

    void removeChair(String tableId, Integer chairPosition);

    void logout(String username);
}
