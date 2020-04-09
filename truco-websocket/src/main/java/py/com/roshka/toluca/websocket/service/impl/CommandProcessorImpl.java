package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.service.CommandProcessor;
import py.com.roshka.toluca.websocket.service.RoomService;
import py.com.roshka.truco.api.TrucoRoom;

import static py.com.roshka.toluca.websocket.global.Commands.*;

@Component
public class CommandProcessorImpl extends Processor implements CommandProcessor {

    RoomService roomService;


    public CommandProcessorImpl(ObjectMapper objectMapper, RoomService roomService) {
        super(objectMapper);
        this.roomService = roomService;
    }

    @Override
    public Event processCommand(Command command) {
        if (CREATE_ROOM.equalsIgnoreCase(command.getCommand())) {
            return getEvent(command, roomService.createRoom(objectMapper.convertValue(command.getData(), TrucoRoom.class)));
        } else if (JOIN_ROOM.equalsIgnoreCase(command.getCommand())) {
            return getEvent(command, roomService.joinRoom(objectMapper.convertValue(command.getData(), TrucoRoom.class)));
        } else if (GET_ROOMS.equalsIgnoreCase(command.getCommand())) {
            return getEvent(command, roomService.findAllRooms());
        } else {
            new IllegalArgumentException("Command not found [" + command.getCommand() + "]");
        }
        return null;
    }


}
