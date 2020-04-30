package py.com.roshka.truco.api;

import py.com.roshka.truco.api.game.TrucoGameRequest;

import java.util.ArrayList;
import java.util.List;

public class TrucoGameEvent extends TrucoRoomTableEvent {
    private Integer id;
    private Player player;
    private String text;
    private List<SpanishCard> cards;
    private SpanishCard card;
    private List<TrucoGameRequest> options;
    private TrucoGameData game;
    private List<TrucoGameMessage> messages = new ArrayList<>();
    private List<TrucoGameEvent> events = null;
    private TrucoRoomTableDescriptor table;
    private Integer from;
    private Integer to;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public List<TrucoGameRequest> getOptions() {
        return options;
    }

    public void setOptions(List<TrucoGameRequest> options) {
        this.options = options;
    }

    public TrucoGameData getGame() {
        return game;
    }

    public void setGame(TrucoGameData game) {
        this.game = game;
    }

    public List<TrucoGameMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<TrucoGameMessage> messages) {
        this.messages = messages;
    }


    public List<TrucoGameEvent> getEvents() {
        return events;
    }

    public void setEvents(List<TrucoGameEvent> events) {
        this.events = events;
    }


    @Override
    public TrucoRoomTableDescriptor getTable() {
        return table;
    }

    @Override
    public void setTable(TrucoRoomTableDescriptor table) {
        this.table = table;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "TrucoGameEvent{" +
                " tid=" + id +
                ", player=" + player +
                ", table=" + table +
                ", cards=" + cards +
                ", card=" + card +
                ", request='" + options + '\'' +
                ", game=" + game +
                ", messages=" + messages +
                ", events=" + events +
                "} " + super.toString();
    }
}
