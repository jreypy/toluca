package py.com.roshka.truco.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/{roomId}")
    public TrucoRoom findRoomById(@PathVariable("roomId") String roomId) {
        return trucoRoomSvc.findRoomById(roomId);
    }

    @PostMapping("")
    public TrucoRoom createTrucoRoom(@RequestBody TrucoRoom trucoRoom) {
        return trucoRoomSvc.create(trucoRoom);
    }

    @PostMapping("/{roomId}/join")
    public TrucoRoomEvent joinTrucoRoom(@PathVariable("roomId") String roomId) {

        return trucoRoomSvc.joinRoom(roomId, trucoUserSvc.getTrucoUser());
    }
}
