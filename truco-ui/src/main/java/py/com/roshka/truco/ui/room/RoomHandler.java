package py.com.roshka.truco.ui.room;

import org.apache.log4j.Logger;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.api.TrucoRoomTableDescriptor;
import py.com.roshka.truco.ui.room.table.TableHandler;
import py.edu.uca.fcyt.toluca.RoomClient;
import py.edu.uca.fcyt.toluca.guinicio.RoomUING;

import java.util.HashMap;
import java.util.Map;

public class RoomHandler {
    static Logger logger = Logger.getLogger(RoomHandler.class);
    public static TrucoPrincipal principal = null;

    static private Map<String, RoomHandler> rooms = new HashMap<>();

    final private String id;
    private final RoomUING rui;
    private RoomClient roomClient;
    private PanelComandos2 panelComandos2;
    private TableGame2 tableGame2;
    private Map<String, TableHandler> tablesHandlers = new HashMap<>();
    private Map<String, TrucoRoomTableDescriptor> tables = new HashMap<>();

    public RoomHandler(String id, RoomUING rui) {
        this.id = id;
        this.rui = rui;
        tableGame2 = new TableGame2();
    }

    public static RoomHandler getRoomHandler(String roomId) {
        RoomHandler roomClient2 = rooms.get(roomId);
        return roomClient2;
    }

    synchronized public static RoomHandler create(String roomId, RoomUING rui) {
        RoomHandler roomHandler = new RoomHandler(roomId, rui);
        rooms.put(roomId, roomHandler);
        return roomHandler;
    }

    public RoomClient getRoomClient2() {
        if (roomClient == null) {
            roomClient = new RoomClient(rui);
            panelComandos2 = new PanelComandos2(this);
            panelComandos2.setRoomClient(roomClient);
            rui.getChatPanel().setCpc(roomClient);
            roomClient.setChatPanel(rui.getChatPanel());
            roomClient.setMainTable(rui.getTableGame());
            roomClient.setRankTable(rui.getTableRanking());
        }
        return roomClient;
    }


    public TableGame2 getTableGame2() {

        return tableGame2;
    }

    public void setTableGame2(TableGame2 tableGame2) {
        this.tableGame2 = tableGame2;
    }

    public PanelComandos2 getPanelComandos2() {
        return panelComandos2;
    }

    public void setPanelComandos2(PanelComandos2 panelComandos2) {
        this.panelComandos2 = panelComandos2;
    }


    public TableHandler addTable(TrucoRoomTableDescriptor table) {
        tables.put(table.getId(), table);
        tableGame2.insertarFila(table.getId());
        TableHandler tableHandler = getTableHandler(table);
        if (table.getOwner().getUsername().equalsIgnoreCase(RoomHandler.principal.getUsername())){
            tableHandler.show();
        }
        return tableHandler;
    }

    public TableHandler getTableHandler(String tableId) {
        return tablesHandlers.get(tableId);
    }

    synchronized public TableHandler getTableHandler(TrucoRoomTableDescriptor tableDescriptor) {
        TableHandler tableHandler = tablesHandlers.get(tableDescriptor.getId());
        if (tableHandler == null) {
            tableHandler = new TableHandler(tableDescriptor);
            tablesHandlers.put(tableDescriptor.getId(), tableHandler);
        }
        return tableHandler;
    }


}
