package py.com.roshka.truco.server.controller;

import org.springframework.web.bind.annotation.*;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.server.beans.AuthResponse;
import py.com.roshka.truco.server.exceptions.LoginException;
import py.com.roshka.truco.server.service.AuthSvc;

import java.util.Map;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    AuthSvc authSvc;

    public AuthController(AuthSvc authSvc) {
        this.authSvc = authSvc;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody Map<String, String> map) throws LoginException {
        return authSvc.login(map.get("username"), map.get("password"));
    }


}
