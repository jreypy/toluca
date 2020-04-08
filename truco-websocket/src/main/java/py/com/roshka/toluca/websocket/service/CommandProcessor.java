package py.com.roshka.toluca.websocket.service;

import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.Event;

public interface CommandProcessor {

    Event processCommand(String token, Command command);
}
