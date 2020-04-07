package py.com.roshka.truco.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.server.service.TrucoRoomSvc;

import java.util.List;

@RestController()
@RequestMapping("/api/room")
public class TrucoRoomController {
    TrucoRoomSvc trucoRoomSvc;

    public TrucoRoomController(TrucoRoomSvc trucoRoomSvc) {
        this.trucoRoomSvc = trucoRoomSvc;
    }

    @GetMapping("")
    public List<TrucoRoom> findAllTrucoRoom() {
        return trucoRoomSvc.findAllRooms();
    }
}
