package py.com.roshka.truco.api;

public class Event {

    public static String TRUCO_ROOM_EVENT = "TRUCO_ROOM_EVENT";
    public static String TRUCO_TABLE_EVENT = "TRUCO_TABLE_EVENT";
    public static String TRUCO_GAME_EVENT = "TRUCO_GAME_EVENT";


    public static String LOGOUT = "LOGOUT";
    public static String ROOMS_FOUND = "ROOMS_FOUND";
    public static String ROOM_FOUND = "ROOM_FOUND";

    public static String ROOM_CREATED = "ROOM_CREATED";
    public static String TABLE_POSITION_SETTED = "TABLE_POSITION_SETTED";

    public static String ROOM_USER_JOINED = "ROOM_USER_JOINED";
    public static String ROOM_USER_LEFT = "USER_LEFT_ROOM";

    public static String COMMAND_RESPONSE = "COMMAND_RESPONSE";
    // Truco Table
    public static String ROOM_TABLE_CREATED = "ROOM_TABLE_CREATED";
    public static String ROOM_TABLE_STATUS_UPDATED = "ROOM_TABLE_STATUS_UPDATED";
    public static String ROOM_TABLE_USER_JOINED = "ROOM_TABLE_USER_JOINED";

    public static String TRUCO_GAME_REQUEST = "TRUCO_GAME_REQUEST";

    // Truco Game
    public static String GAME_STARTED = "GAME_STARTED";
    public static String HAND_STARTED = "HAND_STARTED";
    public static String HAND_ENDED = "HAND_ENDED";

    public static String GIVING_CARDS = "GIVING_CARDS";

    public static String PLAY_CARD = "PLAY_CARD";

    public static String PLAY_REQUEST = "PLAY_REQUEST";
    public static String PLAY_REQUEST_CARD = "CARD";
    public static String PLAY_REQUEST_TRUCO = "TRUCO";
    public static String PLAY_REQUEST_RETRUCO = "RETRUCO";
    public static String PLAY_REQUEST_VALE_CUATRO = "VALE_CUATRO";
    public static String PLAY_REQUEST_ENVIDO = "ENVIDO";
    public static String PLAY_REQUEST_FALTA_ENVIDO = "FALTA_ENVIDO";
    public static String PLAY_REQUEST_REAL_ENVIDO = "REAL_ENVIDO";
    public static String PLAY_REQUEST_FLOR = "FLOR";
    public static final String PLAY_REQUEST_CONTRAFLOR = "COTRAFLOR";
    public static final String PLAY_REQUEST_ENVIDO_VALUE = "ENVIDO_VALUE";
    public static final String PLAY_REQUEST_FLOR_VALUE = "FLOR_VALUE";


    //
    public static String PLAY_RESPONSE = "PLAY_RESPONSE";


}
