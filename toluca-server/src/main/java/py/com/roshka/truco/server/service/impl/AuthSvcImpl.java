package py.com.roshka.truco.server.service.impl;

import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.server.beans.AuthResponse;
import py.com.roshka.truco.server.exceptions.LoginException;
import py.com.roshka.truco.server.service.AuthSvc;
import py.com.roshka.truco.server.service.TrucoRoomSvc;

import java.util.UUID;

@Component
public class AuthSvcImpl implements AuthSvc {

    TrucoRoomSvc trucoRoomSvc;

    public AuthSvcImpl(TrucoRoomSvc trucoRoomSvc) {
        this.trucoRoomSvc = trucoRoomSvc;
    }

    @Override
    public TrucoPrincipal login(String username, String password) throws LoginException {
        // Notificar Nuevo Usuario
        return new TrucoPrincipal(username, username + "-" + UUID.randomUUID().toString());
    }


}
