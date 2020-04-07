package py.com.roshka.truco.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
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
    public List<TrucoRoom> findAllTrucoRoom(@Value("#{request}") Object request) {
        return trucoRoomSvc.findAllRooms();
    }

    @GetMapping("/{id}")
    public TrucoRoom findAllTrucoRoom(@PathVariable("id") String id) {
        return trucoRoomSvc.findAllRooms().get(0);
    }
}
