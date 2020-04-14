package py.com.roshka.truco.server.controller;

import org.springframework.web.bind.annotation.*;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.TrucoRoomTableEvent;
import py.com.roshka.truco.server.service.TrucoRoomSvc;
import py.com.roshka.truco.server.service.TrucoUserService;

@RestController()
@RequestMapping("/api/room/{roomId}/table")
public class TrucoRoomTableController {

    TrucoRoomSvc trucoRoomSvc;

    TrucoUserService trucoUserSvc;

    public TrucoRoomTableController(TrucoRoomSvc trucoRoomSvc, TrucoUserService trucoUserSvc) {
        this.trucoRoomSvc = trucoRoomSvc;
        this.trucoUserSvc = trucoUserSvc;
    }

    @PostMapping("")
    public TrucoRoomEvent createTrucoRoom(@PathVariable("roomId") String roomId, @RequestBody TrucoRoomTable trucoRoomTable) {
        return trucoRoomSvc.addTable(roomId, trucoRoomTable);
    }

    @PutMapping("{tableId}/position/{position}")
    public TrucoRoomTableEvent setTablePosition(@PathVariable("roomId") String roomId, @PathVariable("tableId") String tableId, @PathVariable("position") Integer position) {
        return trucoRoomSvc.setTablePosition(roomId, tableId, position);
    }

    @PostMapping("/{tableId}/join")
    public TrucoRoomEvent joinTrucoRoomTable(@PathVariable("roomId") String roomId, @PathVariable("tableId") String tableId) {
        return trucoRoomSvc.joinRoomTable(roomId, tableId);
    }


}
