package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.CommandResponse;
import py.com.roshka.toluca.websocket.service.CommandProcessor;
import py.com.roshka.toluca.websocket.service.RoomService;
import py.com.roshka.truco.api.TrucoGamePlay;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.request.JoinRoomTableRequest;
import py.com.roshka.truco.api.request.StartGameRequest;
import py.com.roshka.truco.api.request.TablePositionRequest;

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
        if (PLAY.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.play(objectMapper.convertValue(command.getData(), TrucoGamePlay.class)));
        }
        else if (START_GAME.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.startGame(objectMapper.convertValue(command.getData(), StartGameRequest.class)));
        } else if (JOIN_ROOM_TABLE.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.joinRoomTable(objectMapper.convertValue(command.getData(), JoinRoomTableRequest.class)));
        } else if (CREATE_ROOM_TABLE.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.createRoomTable(objectMapper.convertValue(command.getData(), TrucoRoomTable.class)));
        } else if (SET_TABLE_POSITION.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.setTablePosition(objectMapper.convertValue(command.getData(), TablePositionRequest.class)));
        } else if (JOIN_ROOM.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.joinRoom(objectMapper.convertValue(command.getData(), TrucoRoom.class)));
        } else if (GET_ROOMS.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.findAllRooms());
        } else if (GET_ROOM.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.findRoomById(objectMapper.convertValue(command.getData(), TrucoRoom.class).getId()));
        } else if (CREATE_ROOM.equalsIgnoreCase(command.getCommand())) {
            return getCommandResponse(command, roomService.createRoom(objectMapper.convertValue(command.getData(), TrucoRoom.class)));
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
