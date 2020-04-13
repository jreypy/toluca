package py.com.roshka.truco.api;

public class TrucoGamePlay {
    static public final String PLAY_CARD = "PLAY_CARD";

    private String tableId;
    private String roomId;
    private String type;
    private SpanishCard card;
    private Integer envido;
    private Integer flor;
    private String word;
    private Player player;

    public TrucoGamePlay() {
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SpanishCard getCard() {
        return card;
    }

    public void setCard(SpanishCard card) {
        this.card = card;
    }

    public Integer getEnvido() {
        return envido;
    }

    public void setEnvido(Integer envido) {
        this.envido = envido;
    }

    public Integer getFlor() {
        return flor;
    }

    public void setFlor(Integer flor) {
        this.flor = flor;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "TrucoGamePlay{" +
                "tableId='" + tableId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", type='" + type + '\'' +
                ", card=" + card +
                ", envido=" + envido +
                ", flor=" + flor +
                ", word='" + word + '\'' +
                ", player=" + player +
                '}';
    }
}
