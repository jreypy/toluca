package py.com.roshka.truco.server.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.server.service.TrucoRoomTableSvc;

@RestController()
@RequestMapping("/api/room/{roomId}/table/{tableId}")
public class TrucoGameController {
    TrucoRoomTableSvc trucoRoomTableSvc;

    public TrucoGameController(TrucoRoomTableSvc trucoRoomTableSvc) {
        this.trucoRoomTableSvc = trucoRoomTableSvc;
    }

    @PostMapping("/start-game")
    public TrucoGameEvent startGame(@PathVariable("roomId") String roomId, @PathVariable("tableId") String tableId) {
        return trucoRoomTableSvc.startGame(roomId, tableId);
    }
}
