package py.com.roshka.toluca.robot;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.constants.Commands;
import py.com.roshka.truco.api.request.JoinRoomTableRequest;
import py.com.roshka.truco.api.request.TablePositionRequest;

import java.util.Map;

public class DummyBot extends TolucaBot {
    static Logger logger = Logger.getLogger(DummyBot.class);


    @Override
    String getName() {
        return "Robot";
    }

    @Override
    void login() {
        executeCommand(Commands.GET_ROOM, MAIN_ROOM);
        executeCommand(Commands.JOIN_ROOM, MAIN_ROOM);
    }

    @Override
    void receiveEvent(Map e) {
        logger.info("Message received [" + e + "]");
        if (Event.ROOM_TABLE_CREATED.equalsIgnoreCase((String) e.get("type"))) {
            Map map = (Map) e.get("data");
            // JOIN
            JoinRoomTableRequest joinRoomTable = new JoinRoomTableRequest();
            joinRoomTable.setRoomId((String) map.get("roomId"));
            joinRoomTable.setTableId((String) map.get("id"));
            // Join Room table
            executeCommand(Commands.JOIN_ROOM_TABLE, joinRoomTable);

            TablePositionRequest tablePositionRequest = new TablePositionRequest();
            tablePositionRequest.setRoomId(joinRoomTable.getRoomId());
            tablePositionRequest.setTableId(joinRoomTable.getTableId());
            tablePositionRequest.setChair(1);
            executeCommand(Commands.SET_TABLE_POSITION, tablePositionRequest);
            // Sit
        }

    }

    static {
        BasicConfigurator.configure();
    }

    public static void main(String[] args) {
        DummyBot dummyBot = new DummyBot();
        Thread t = new Thread(dummyBot);
        t.start();
    }

}
