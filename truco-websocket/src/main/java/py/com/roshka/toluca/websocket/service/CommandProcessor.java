package py.com.roshka.toluca.websocket.service;

import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.CommandResponse;
import py.com.roshka.toluca.websocket.beans.Event;

public interface CommandProcessor {

    CommandResponse processCommand(Command command);
}
