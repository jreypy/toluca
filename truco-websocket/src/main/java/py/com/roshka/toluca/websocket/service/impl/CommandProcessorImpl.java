package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.service.CommandProcessor;

import static py.com.roshka.toluca.websocket.global.Commands.*;

import py.com.roshka.toluca.websocket.service.CommandProcessor;
import py.com.roshka.toluca.websocket.service.RoomService;
import py.com.roshka.truco.api.TrucoRoom;

import java.util.List;
import java.util.Map;

@Component
public class CommandProcessorImpl implements CommandProcessor {
    private ObjectMapper objectMapper;
    RoomService roomService;

    public CommandProcessorImpl(ObjectMapper objectMapper, RoomService roomService) {
        this.objectMapper = objectMapper;
        this.roomService = roomService;
    }

    @Override
    public Event processCommand(String token, Command command) {
        if (CREATE_ROOM.equalsIgnoreCase(command.getCommand())) {
            return getEvent(command, roomService.createRoom(objectMapper.convertValue(command.getData(), TrucoRoom.class)));
        } else if (GET_ROOMS.equalsIgnoreCase(command.getCommand())) {
            return getEvent(command, roomService.findAllRooms());
        } else {
            new IllegalArgumentException("Command not found [" + command.getCommand() + "]");
        }
        return null;
    }

    public Event getEvent(Command command, List list) {
        return new Event(command.getCommand(), list);
    }

    public Event getEvent(Command command, Object object) {
        return new Event(command.getCommand(), objectMapper.convertValue(object, Map.class));
    }


}
