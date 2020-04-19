package py.com.roshka.truco.ui;

import org.apache.log4j.Logger;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomDescriptor;
import py.com.roshka.truco.ui.room.PanelComandos2;
import py.com.roshka.truco.ui.room.RoomClient2;
import py.com.roshka.truco.ui.room.RoomHandler;
import py.com.roshka.truco.ui.room.TableGame2;
import py.edu.uca.fcyt.net.CommunicatorProvider;
import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.guinicio.PanelComandos;
import py.edu.uca.fcyt.toluca.guinicio.RoomUING;
import py.edu.uca.fcyt.toluca.guinicio.TableGame;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class TrucoFrame extends RoomUING {
    public static String MAIN_ROOM_ID = "1";
    public static TrucoRoom MAIN_ROOM = new TrucoRoom(MAIN_ROOM_ID, MAIN_ROOM_ID);

    static Logger logger = Logger.getLogger(TrucoFrame.class);


    JFrame frameContainer;
    private TableGame newTableGame = null;

    private String roomId = MAIN_ROOM_ID;
    final private RoomHandler roomHandler;

    public TrucoFrame(JFrame frameContainer) {
        this.frameContainer = frameContainer;
        CommunicatorProvider.intanceType = WebSocketCommunicatorClient.class;
        roomHandler = RoomHandler.create(MAIN_ROOM_ID, this);
        init();
        setContentPane(getCcontenPane());
    }

    public RoomClient getRoomClient() {
        return roomHandler.getRoomClient2();

    }

    @Override
    public void panelUpdated() {
        logger.debug("Panel Updated");
        frameContainer.pack();
    }

    public String getParameter(String name) {
        return "";
    }

    public TableGame getTableGame() {
        return roomHandler.getTableGame2();
    }

    @Override
    protected PanelComandos getPanelComandos() {
        return roomHandler.getPanelComandos2();
    }
}
