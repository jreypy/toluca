package py.com.roshka.truco.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.com.roshka.truco.server.service.TrucoRoomSvc;
import py.com.roshka.truco.server.service.TrucoUserService;

import java.util.List;

@RestController()
@RequestMapping("/api/room")
public class TrucoRoomController {
    TrucoRoomSvc trucoRoomSvc;

    TrucoUserService trucoUserSvc;

    public TrucoRoomController(TrucoRoomSvc trucoRoomSvc, TrucoUserService trucoUserSvc) {
        this.trucoRoomSvc = trucoRoomSvc;
        this.trucoUserSvc = trucoUserSvc;
    }

    @GetMapping("")
    public List<TrucoRoom> findAllTrucoRoom(@Value("#{request}") Object request) {
        return trucoRoomSvc.findAllRooms();
    }

    @GetMapping("/{id}")
    public TrucoRoom findAllTrucoRoom(@PathVariable("id") String id) {
        return trucoRoomSvc.findAllRooms().get(0);
    }

    @PostMapping("")
    public TrucoRoom createTrucoRoom(@RequestBody TrucoRoom trucoRoom) {
        return trucoRoomSvc.create(trucoRoom);
    }

    @PostMapping("/{roomId}/join")
    public TrucoRoomEvent joinTrucoRoom(@PathVariable("roomId") String roomId, @RequestParam("authkey") String authkey) {
        return trucoRoomSvc.joinRoom(roomId, trucoUserSvc.getTrucoUser(authkey));
    }
}
