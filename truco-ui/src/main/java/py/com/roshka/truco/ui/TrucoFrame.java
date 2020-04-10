package py.com.roshka.truco.ui;

import org.apache.log4j.Logger;
import py.com.roshka.truco.api.TrucoRoom;
import py.edu.uca.fcyt.net.CommunicatorProvider;
import py.edu.uca.fcyt.toluca.guinicio.RoomUING;

import javax.swing.*;

public class TrucoFrame extends RoomUING {
    public static String MAIN_ROOM_ID = "1";
    public static TrucoRoom MAIN_ROOM = new TrucoRoom(MAIN_ROOM_ID);

    static Logger logger = Logger.getLogger(TrucoFrame.class);


    JFrame frameContainer;

    public TrucoFrame(JFrame frameContainer) {
        this.frameContainer = frameContainer;
        CommunicatorProvider.intanceType = WebSocketCommunicatorClient.class;
        init();
        setContentPane(getCcontenPane());
    }

    @Override
    public void panelUpdated() {
        logger.debug("Panel Updated");
        frameContainer.pack();
    }

    public String getParameter(String name) {
        return "";
    }


}
