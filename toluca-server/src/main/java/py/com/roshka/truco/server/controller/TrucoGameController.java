package py.com.roshka.truco.server.controller;

import org.springframework.web.bind.annotation.*;
import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.api.TrucoGamePlay;
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

    @PostMapping("/start-hand")
    public TrucoGameEvent startHand(@PathVariable("roomId") String roomId, @PathVariable("tableId") String tableId) {
        return trucoRoomTableSvc.startHand(roomId, tableId);
    }

    @PostMapping("/play")
    public TrucoGameEvent play(@PathVariable("roomId") String roomId, @PathVariable("tableId") String tableId, @RequestBody TrucoGamePlay trucoGamePlay) {
        return trucoRoomTableSvc.play(roomId, tableId, trucoGamePlay);
    }
}
