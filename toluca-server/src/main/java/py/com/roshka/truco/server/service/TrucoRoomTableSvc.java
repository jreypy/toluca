package py.com.roshka.truco.server.service;

import py.com.roshka.truco.api.TrucoGameEvent;

public interface TrucoRoomTableSvc {



    public TrucoGameEvent startGame(String roomId, String tableId);
}
