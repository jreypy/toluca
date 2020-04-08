package py.com.roshka.truco.server.service;

import py.com.roshka.truco.api.TrucoUser;

public interface TrucoUserService {
    TrucoUser getTrucoUser(String authKey);
}
