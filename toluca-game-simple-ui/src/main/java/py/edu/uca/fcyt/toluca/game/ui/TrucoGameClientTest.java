package py.edu.uca.fcyt.toluca.game.ui;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.constants.Commands;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.com.roshka.truco.api.request.JoinRoomTableRequest;
import py.com.roshka.truco.api.request.RoomRequest;
import py.com.roshka.truco.client.communication.TrucoClient;
import py.com.roshka.truco.client.communication.TrucoClientHandler;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;
import py.com.roshka.truco.client.communication.impl.TrucoClientImpl;
import py.edu.uca.fcyt.toluca.game.InvalidPlayExcepcion;
import py.edu.uca.fcyt.toluca.game.TrucoGame;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TrucoGameClientTest extends TrucoGame implements TrucoClientHandler {
    Logger logger = LoggerFactory.getLogger(TrucoGameClientTest.class);

    public static String MAIN_ROOM_ID = "1";


    TPlayer tPlayer;
    TrucoPrincipal trucoPrincipal;
    TrucoClient trucoClient;
    String username;
    private boolean starter = false;
    TrucoGameClientTest[] players = null;
    ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    int size = 2;
    String tableId = null;


    Map<String, TrucoPlayer> trucoPlayersMap = new HashMap<String, TrucoPlayer>();


    public TrucoGameClientTest(String username) throws Exception {
        TrucoPlayer trucoPlayer = new TrucoPlayer(username);
        this.username = username;
    }

    public TrucoGameClientTest(String username, TrucoGameClientTest[] players) throws Exception {
        TrucoPlayer trucoPlayer = new TrucoPlayer(username);
        trucoPlayersMap.put(trucoPlayer.getName(), trucoPlayer);
        tPlayer = new TPlayer(trucoPlayer, this, 2);
        this.username = username;
        this.starter = true;
        this.players = players;
    }

    public void connect() throws TrucoClientException {
        connect(null);
    }

    public void connect(String tableId) throws TrucoClientException {
        this.tableId = tableId;
        trucoClient = new TrucoClientImpl("http://localhost:8091", "ws://localhost:8050");
        trucoClient.login(username, username, this);
    }


    public void startGame() {

    }

    public void startHand(TrucoPlayer tPlayer) {

    }

    public boolean esPosibleJugar(TrucoPlay tp) {
        return false;
    }

    public void play(TrucoPlay tp) throws InvalidPlayExcepcion {

    }

    public Vector getDetallesDeLaMano() {
        return null;
    }

    public void loginFailed() {

    }

    public void afterLogin(TrucoPrincipal trucoPrincipal) throws TrucoClientException {
        this.trucoPrincipal = trucoPrincipal;
        trucoClient.connect();
    }

    public void receiveMessage(Object object) {
        logger.debug("Event Received [" + object + "]");

        Map e = objectMapper.convertValue(object, Map.class);
        // logger.info("Message received [" + e + "]");
        String type = (String) e.get("type");


        if (Event.COMMAND_RESPONSE.equalsIgnoreCase(type)) {
            String command = (String) e.get("command");
            if (command.equals(Commands.JOIN_ROOM)) {
                if (starter) {
                    //$ {"command":"create_room_table", "data" : {"roomId":"1", "points": 30 }}
                    TrucoRoomTable trucoRoomTable = new TrucoRoomTable();
                    trucoRoomTable.setRoomId(MAIN_ROOM_ID);
                    trucoRoomTable.setPoints(30);
                    executeCommand(Commands.CREATE_ROOM_TABLE, trucoRoomTable);
                } else {
                    //{"command":"join_room_table", "data" : {"roomId":"1","tableId":"1"}}
                    JoinRoomTableRequest joinRoomTableRequest = new JoinRoomTableRequest();
                    joinRoomTableRequest.setTableId(this.tableId);
                    joinRoomTableRequest.setRoomId(MAIN_ROOM_ID);
                    executeCommand(Commands.JOIN_ROOM_TABLE, joinRoomTableRequest);
                }
            } else if (command.equals(Commands.CREATE_ROOM_TABLE)) {
                TrucoRoomEvent trucoRoomEvent = objectMapper.convertValue(e.get("data"), TrucoRoomEvent.class);
                try {
                    tPlayer = new TPlayer(new TrucoPlayer(username), this, size);
                    tPlayer.setVisible(true);
                    for (int i = 0; i < this.players.length; i++) {
                        TrucoGameClientTest trucoGameClientTest = this.players[i];
                        trucoGameClientTest.connect(trucoRoomEvent.getTable().getId());
                    }
                } catch (Exception e1) {
                    System.out.println("Error!!");
                }
            } else if (Commands.JOIN_ROOM_TABLE.equalsIgnoreCase(command)) {
                try {
                    tPlayer = new TPlayer(new TrucoPlayer(username), this, size);
                    tPlayer.setVisible(true);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
//            Event event  = objectMapper.convertValue(e.get("data"), )
        } else if (Event.TRUCO_ROOM_EVENT.equalsIgnoreCase(type)) {
            TrucoGameEvent trucoGameEvent = objectMapper.convertValue(e.get("data"), TrucoGameEvent.class);
            String eventName = trucoGameEvent.getEventName();
            if (Event.ROOM_TABLE_USER_JOINED.equalsIgnoreCase(eventName)) {
//                trucoGameEvent.getPlayer()
                trucoPlayersMap.put(trucoGameEvent.getUser().getUsername(), TolucaHelper.getPlayer(trucoGameEvent.getUser()));
                if (starter) {
                    if (trucoPlayersMap.size() == size) {
                        System.out.println("================ YA ESTAN TODOS EN LA MESA  =========");
                    } else {
                        System.out.println("================ NO ESTAN TODOS EN LA MESA  ========= [" + trucoPlayersMap.size() + "]");
                    }
                }
            } else {
                System.err.println("*******   No Se maneja el evento [" + eventName + "]");
            }
        } else {
            System.err.println("*******   No se maneja el tipo de Evento [" + type + "]");
        }

//        if (Event.COMMAND_RESPONSE.equalsIgnoreCase(type)) {
//            logger.debug("Command Response [" + e + "]");
//        }
//        if (Event.ROOM_USER_JOINED.equalsIgnoreCase()){
//
//        }
//        if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase(type)) {
//            try {
//                tPlayer = new TPlayer(new TrucoPlayer(username), this, size);
//            } catch (Exception e1) {
//                logger.error(e1.getMessage(), e);
//            }
////            Map map = (Map) e.get("data");
////            // JOIN
////            JoinRoomTableRequest joinRoomTable = new JoinRoomTableRequest();
////            joinRoomTable.setRoomId((String) map.get("roomId"));
////            joinRoomTable.setTableId((String) map.get("id"));
////            // Join Room table
////            executeCommand(Commands.JOIN_ROOM_TABLE, joinRoomTable);
////
////            TablePositionRequest tablePositionRequest = new TablePositionRequest();
////            tablePositionRequest.setRoomId(joinRoomTable.getRoomId());
////            tablePositionRequest.setTableId(joinRoomTable.getTableId());
////            tablePositionRequest.setChair(1);
////            executeCommand(Commands.SET_TABLE_POSITION, tablePositionRequest);
//            // Sit
//        }
    }

    public void ready() {
        logger.debug("User [" + username + "] is ready!!!");
        executeCommand(Commands.GET_ROOM, new RoomRequest(MAIN_ROOM_ID));
        executeCommand(Commands.JOIN_ROOM, new RoomRequest(MAIN_ROOM_ID));

    }

    protected void executeCommand(String commandName, Object message) {
        try {
            logger.debug("Executing Command [" + commandName + "] [" + message + "]");
            trucoClient.send(commandName, message);
        } catch (TrucoClientException e) {
            e.printStackTrace();
        }
    }
}
