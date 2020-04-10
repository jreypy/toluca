package py.com.roshka.truco.server.controller;

import org.springframework.web.bind.annotation.*;
import py.com.roshka.truco.api.TrucoRoomTable;
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
    public TrucoRoomTable createTrucoRoom(@PathVariable("roomId") String roomId, @RequestBody TrucoRoomTable trucoRoomTable) {
        return trucoRoomSvc.addTable(roomId, trucoRoomTable);
    }
}
