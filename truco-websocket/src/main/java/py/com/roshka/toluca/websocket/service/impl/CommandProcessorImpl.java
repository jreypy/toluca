package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.CommandResponse;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.service.CommandProcessor;
import py.com.roshka.toluca.websocket.service.RoomService;
import py.com.roshka.truco.api.TrucoRoom;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static py.com.roshka.truco.api.constants.Commands.*;

@Component
public class CommandProcessorImpl extends Processor implements CommandProcessor {

    RoomService roomService;


    public CommandProcessorImpl(ObjectMapper objectMapper, RoomService roomService) {
        super(objectMapper);
        this.roomService = roomService;
    }

    @Override
    public CommandResponse processCommand(Command command) {
        if (CREATE_ROOM.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.createRoom(objectMapper.convertValue(command.getData(), TrucoRoom.class)));
        } else if (JOIN_ROOM.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.joinRoom(objectMapper.convertValue(command.getData(), TrucoRoom.class)));
        } else if (GET_ROOMS.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.findAllRooms());
        } else if (GET_ROOM.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.findRoomById(objectMapper.convertValue(command.getData(), TrucoRoom.class).getId()));
        } else {
            new IllegalArgumentException("Command not found [" + command.getCommand() + "]");
        }
        return null;
    }

    CommandResponse getCommandResponse(Command command, Object data) {
        return new CommandResponse(command.getCommand(), command.getId(), data);
    }

    CommandResponse getCommandResponse(Command command, List data) {
        Map map = new LinkedHashMap();
        map.put("items", data);
        return new CommandResponse(command.getCommand(), command.getId(), map);
    }


}
