package py.com.roshka.truco.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.com.roshka.truco.server.service.TrucoRoomSvc;
import py.com.roshka.truco.server.service.TrucoUserService;

@RestController()
@RequestMapping("/api/room")
public class TrucoRoomController {
    TrucoRoomSvc trucoRoomSvc;


    public TrucoRoomController(TrucoRoomSvc trucoRoomSvc) {
        this.trucoRoomSvc = trucoRoomSvc;
    }

    @GetMapping("")
    public TrucoRoomEvent findAllTrucoRoom(@Value("#{request}") Object request) {
        return trucoRoomSvc.findAllRooms();
    }

    @GetMapping("/{roomId}")
    public TrucoRoomEvent findRoomById(@PathVariable("roomId") String roomId) {
        return trucoRoomSvc.findRoomById(roomId);
    }

    @PostMapping("")
    public TrucoRoomEvent createTrucoRoom(@RequestBody TrucoRoom trucoRoom) {
        return trucoRoomSvc.create(trucoRoom);
    }

    @PostMapping("/{roomId}/join")
    public TrucoRoomEvent joinTrucoRoom(@PathVariable("roomId") String roomId) {
        return trucoRoomSvc.joinRoom(roomId);
    }
}
