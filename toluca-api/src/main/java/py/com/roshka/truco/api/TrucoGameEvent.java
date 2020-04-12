package py.com.roshka.truco.api;

import java.util.List;

public class TrucoGameEvent extends TrucoRoomTableEvent {
    private Player player;
    private List<SpanishCard> cards;
    private SpanishCard card;
    private String request;
    private TrucoGameData game;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<SpanishCard> getCards() {
        return cards;
    }

    public void setCards(List<SpanishCard> cards) {
        this.cards = cards;
    }

    public SpanishCard getCard() {
        return card;
    }

    public void setCard(SpanishCard card) {
        this.card = card;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public TrucoGameData getGame() {
        return game;
    }

    public void setGame(TrucoGameData game) {
        this.game = game;
    }
}
