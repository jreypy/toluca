package py.com.roshka.truco.ui.net;

import org.apache.log4j.Logger;
import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.com.roshka.truco.api.TrucoRoomTableEvent;
import py.com.roshka.truco.api.TrucoUser;
import py.com.roshka.truco.ui.Toluca;
import py.com.roshka.truco.ui.WebSocketCommunicatorClient;
import py.com.roshka.truco.ui.event.RoomEvent2;
import py.com.roshka.truco.ui.event.TableEvent2;
import py.com.roshka.truco.ui.event.TrucoEvent2;
import py.com.roshka.truco.ui.room.RoomHandler;
import py.com.roshka.truco.ui.room.game.TrucoGameClient2;
import py.com.roshka.truco.ui.room.table.TableHandler;
import py.edu.uca.fcyt.toluca.Room;
import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.event.RoomEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;
import py.edu.uca.fcyt.toluca.net.EventDispatcherClient;
import py.edu.uca.fcyt.toluca.sound.PlaySound;
import py.edu.uca.fcyt.toluca.table.TableException;

public class EventDispatcherClient2 extends EventDispatcherClient {
    static Logger logger = Logger.getLogger(WebSocketCommunicatorClient.class);


    RoomClient roomClient;

    public EventDispatcherClient2(WebSocketCommunicatorClient webSocketCommunicatorClient) {
        super();
        setCommClient(webSocketCommunicatorClient);

    }

    @Override
    public void setRoom(Room room) {
        roomClient = (RoomClient) room;
        super.setRoom(room);
    }


    public <T> T getSource(Class<T> t, TableEvent event) {
        return t.cast(((TableEvent2) event).getSource());
    }

    public <T> T getSource(Class<T> t, RoomEvent event) {
        return t.cast(((RoomEvent2) event).getSource());
    }

    public <T> T getSource(Class<T> t, py.edu.uca.fcyt.toluca.event.TrucoEvent event) {
        return t.cast(((TrucoEvent2) event).getSource());
    }


    public RoomHandler getRoomHandler(String roomId) {
        return RoomHandler.getRoomHandler(roomId);
    }

    synchronized public void playerJoined(RoomEvent event) {
        TrucoRoomEvent trucoRoomEvent = getSource(TrucoRoomEvent.class, event);
        logger.debug("User joined [" + event + "]");
        if (!RoomHandler.principal.getUsername().equals(trucoRoomEvent.getUser().getUsername())) {
            roomClient.addPlayer(Toluca.getTrucoPlayer(trucoRoomEvent.getUser()));
            roomClient.showSystemMessage(trucoRoomEvent.getUser().getUsername() + " se ha unido al Room");
        } else
            roomClient.addPlayer(trucoPlayer);
    }

//    private void userTableJoined(String roomId, String tableId, TrucoUser user) {
//        TrucoRoomTableEvent trucoRoomTableEvent = new TrucoRoomTableEvent();
//        trucoRoomTableEvent.setEventName(Event.ROOM_TABLE_USER_JOINED);
//        trucoRoomTableEvent.setRoomId(roomId);
//        trucoRoomTableEvent.setTableId(tableId);
//        trucoRoomTableEvent.setUser(user);
//        ;
//
////        roomTableEventDispatcher.dispatchRoomTableEvent(trucoRoomTableEvent);
//    }

    @Override
    synchronized public void tableJoined(RoomEvent event) {
        TrucoRoomEvent trucoRoomEvent = getSource(TrucoRoomEvent.class, event);
        RoomHandler roomHandler = getRoomHandler(trucoRoomEvent.getRoom().getId());

        TableHandler tableHandler = roomHandler.getTableHandler(trucoRoomEvent.getTable());
        TrucoPlayer playerClient = Toluca.getTrucoPlayer(trucoRoomEvent.getUser());

        // Fix Julio Rey -> el Player ya no está
        if (playerClient != null && tableHandler != null) {
            tableHandler.addPlayer(trucoRoomEvent.getUser());
            if (playerClient.getName().equals(trucoPlayer.getName())) {
                tableHandler.show();
            }
            /*
             * Play sound if the player is inside.
             */
            if (tableHandler.isInside(trucoRoomEvent.getUser())) {
                PlaySound.play(PlaySound.ENTER_SOUND);
            }
        } else {
            logeador.warning("El player o la tabla Ya no están!!![" + playerClient.getName() + "]");
        }
    }


    @Override
    public synchronized void playerSit(TableEvent event) {
        TrucoRoomTableEvent trucoRoomTableEvent = getSource(TrucoRoomTableEvent.class, event);
        RoomHandler roomHandler = getRoomHandler(trucoRoomTableEvent.getRoomId());
        TableHandler tableHandler = roomHandler.getTableHandler(trucoRoomTableEvent.getTableId());
        //logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "La silla de
        try {
            TrucoUser user = trucoRoomTableEvent.getUser();
            tableHandler.sitPlayer(user, trucoRoomTableEvent.getPosition());
            roomClient.setearPlayerTable(Toluca.getTrucoPlayer(user), tableHandler.getGame().getTableNumber(), trucoRoomTableEvent.getPosition());
        } catch (TableException e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
        //logeador.log(TolucaConstants.CLIENT_DEBUG_LOG_LEVEL, "ya se hizo el
        // table.sitPlayer");

    }

    @Override
    public void tableCreated(RoomEvent event) {
        TrucoRoomEvent trucoRoomEvent = getSource(TrucoRoomEvent.class, event);
        RoomHandler roomHandler = getRoomHandler(trucoRoomEvent.getRoom().getId());
        roomHandler.addTable(trucoRoomEvent.getTable());

//        RoomEvent table = new RoomEvent();
//        table.setTableServer(new TableServer());
//
//        //Owner
//        if (trucoRoomEvent.getUser() != null) {
//            table.getTableServer().setHost(TolucaHelper.getPlayer(trucoRoomEvent.getUser()));
//        }
//        table.setType(RoomEvent.TYPE_TABLE_CREATED);
//        //Players
//        table.setPlayers(new LinkedHashMap());
//        //Table
//        table.getTableServer().setTableNumber(Integer.parseInt(trucoRoomEvent.getTable().getId()));
//        table.setTableNumber(table.getTableServer().getTableNumber());
//        table.setGamePoints(trucoRoomEvent.getTable().getPoints());
//
//        // Add To Room
//        RoomHandler roomHandler = RoomHandler.getRoomHandler(trucoRoomEvent.getRoom().getId());
//        if (trucoRoomEvent.getTable().getOwner().getUsername().equals(RoomHandler.principal.getUsername())) {
//            roomHandler.getTrucoGameClient2(trucoRoomEvent, true);
//        }
//        roomHandler.addTable(trucoRoomEvent.getTable());
    }


    @Override
    public void gameStarted(TableEvent event) {
        logger.info("***** GameStarted [" + event + "]");
        TrucoGameEvent trucoGameEvent = getSource(TrucoGameEvent.class, event);
        RoomHandler roomHandler = getRoomHandler(trucoGameEvent.getRoomId());
        TrucoGameClient2 trucoGameClient2 = roomHandler.getTableHandler(trucoGameEvent.getTableId()).getGame();
        TrucoTeam teams[] = trucoGameClient2.getTable().createTeams();
        trucoGameClient2.setTeam(teams[0], teams[1]);
        trucoGameClient2.getTable().startGame(trucoGameClient2);
        trucoGameClient2.startGameClient();

    }


    @Override
    public void receiveCards(py.edu.uca.fcyt.toluca.event.TrucoEvent event) {
        logger.info("***** Receive Cards [" + event.getPlayer() + "][" + event.getCards() + "]");
        TrucoGameEvent trucoGameEvent = getSource(TrucoGameEvent.class, event);
        TrucoGameClient2 trucoGameClient2 = getTrucoGameClient2(event);
        if (RoomHandler.principal.getUsername().equalsIgnoreCase(event.getPlayer().getName())) {
            trucoGameClient2.dealtCards(event.getPlayer(), Toluca.getCards(trucoGameEvent.getCards()));
        }
    }

    @Override
    public void infoGame(TrucoEvent event) {
        logger.info("***** Info Game [" + event.getType() + "][" + event + "]");
        TrucoGameClient2 trucoGameClient2 = getTrucoGameClient2(event);
        if (event.getPlayer() != null) {
            trucoGameClient2.play(event);
        }
    }

    public void receivePlayRequest(TrucoGameEvent trucoGameEvent) {
        logger.info("***** Receive PlayRequest [" + trucoGameEvent.getRequest() + "][" + trucoGameEvent.getPlayer() + "]");
        TrucoEvent2 trucoEvent2 = new TrucoEvent2(trucoGameEvent);
        TrucoGameClient2 trucoGameClient2 = getTrucoGameClient2(trucoEvent2);
        // TODO Calculate Envido
        trucoGameClient2.fireTurnEvent(trucoEvent2.getPlayer(), trucoEvent2.getType(), 20);

    }

    public void playResponse(TrucoGameEvent trucoGameEvent) {
        TrucoEvent2 trucoEvent2 = new TrucoEvent2(trucoGameEvent);
        trucoEvent2.setCard(Toluca.getCard(trucoGameEvent.getCard()));
        TrucoGameClient2 trucoGameClient2 = getTrucoGameClient2(trucoEvent2);
        trucoGameClient2.firePlayResponseEvent(trucoEvent2);
    }


    public void handEnded(TrucoGameEvent trucoGameEvent) {
        TrucoGameClient2 trucoGameClient2 = getTrucoGameClient2(trucoGameEvent.getRoomId(), trucoGameEvent.getTableId());
        trucoGameClient2.andEnded(trucoGameEvent);

    }

    TrucoGameClient2 getTrucoGameClient2(TrucoEvent event) {
        TrucoGameEvent trucoGameEvent = getSource(TrucoGameEvent.class, event);
        return getTrucoGameClient2(trucoGameEvent.getRoomId(), trucoGameEvent.getTableId());
    }

    TrucoGameClient2 getTrucoGameClient2(String roomId, String tableId) {
        RoomHandler roomHandler = getRoomHandler(roomId);
        TrucoGameClient2 trucoGameClient2 = roomHandler.getTableHandler(tableId).getGame();
        return trucoGameClient2;
    }


}
