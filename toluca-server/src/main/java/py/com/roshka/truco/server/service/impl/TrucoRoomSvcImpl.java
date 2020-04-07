package py.com.roshka.truco.server.service.impl;

import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.Chair;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.server.service.TrucoRoomSvc;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrucoRoomSvcImpl implements TrucoRoomSvc {

    List<TrucoRoom> rooms = new ArrayList<>();

    public TrucoRoomSvcImpl() {
        TrucoRoom trucoRoom = new TrucoRoom();
        trucoRoom.setId("principal");
        trucoRoom.setName("Principal");
        this.rooms.add(trucoRoom);
    }

    @Override
    public List<TrucoRoom> findAllRooms() {
        return rooms;
    }

    public TrucoRoom create(TrucoRoom trucoRoom) {
        return null;
    }

    public TrucoRoom delete(TrucoRoom trucoRoom) {
        return null;
    }

    public TrucoRoomTable addTable(String roomId, TrucoRoomTable trucoRoomTable) {
        return null;
    }

    public TrucoRoomTable deleteTable(String roomId, String tableId) {
        return null;
    }

    public void addChair(String tableId, Chair chair) {

    }

    public void removeChair(String tableId, Integer chairPosition) {

    }
}
