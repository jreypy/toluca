package py.com.roshka.truco.server.service;

import py.com.roshka.truco.api.*;

import java.util.List;

public interface TrucoRoomSvc {

    TrucoRoomEvent findAllRooms();

    TrucoRoomEvent findRoomById(String roomId);

    TrucoRoomEvent create(TrucoRoom trucoRoom);

    TrucoRoomEvent joinRoom(String roomId);

    TrucoRoomEvent delete(TrucoRoom trucoRoom);

    TrucoRoomEvent addTable(String roomId, TrucoRoomTable trucoRoomTable);

    TrucoRoomEvent joinRoomTable(String roomId, String tableId);

    TrucoRoomEvent leaveRoomTable(String roomId, String tableId);

    TrucoRoomTableEvent setTablePosition(String roomId, String tableId, Integer position);

    TrucoRoomTable deleteTable(String roomId, String tableId);

    void addChair(String tableId, Chair chair);

    void removeChair(String tableId, Integer chairPosition);

    void logout(String username);
}
