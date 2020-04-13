package py.com.roshka.truco.server.service;

import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.api.TrucoGamePlay;

public interface TrucoRoomTableSvc {

    TrucoGameEvent startGame(String roomId, String tableId);

    TrucoGameEvent play(String roomId, String tableId, TrucoGamePlay trucoGamePlay);


}
