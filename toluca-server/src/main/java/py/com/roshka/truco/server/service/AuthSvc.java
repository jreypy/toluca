package py.com.roshka.truco.server.service;

import py.com.roshka.truco.server.beans.AuthResponse;
import py.com.roshka.truco.server.exceptions.LoginException;

public interface AuthSvc {

    AuthResponse login(String username, String password) throws LoginException;
}
