package py.com.roshka.truco.api.helper;


import py.com.roshka.truco.api.*;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.game.TrucoCard;
import py.edu.uca.fcyt.toluca.game.TrucoPlay;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;

import java.util.ArrayList;
import java.util.List;

public class TolucaHelper {
    static TrucoTeam getTeam(TrucoGameTeam trucoGameTeam) {
        TrucoTeam trucoTeam = new TrucoTeam();
        trucoGameTeam.setPlayers(new ArrayList<>());
        for (Player player : trucoGameTeam.getPlayers()) {
            trucoTeam.getPlayers().add(getPlayer(player));
        }
        return trucoTeam;
    }


    public static TrucoPlayer getPlayer(Player player) {
        if (player == null)
            return null;

        return new TrucoPlayer(player.getId());
    }

    public static Player getPlayer(TrucoPlayer trucoPlayer) {
        if (trucoPlayer == null)
            return null;

        return new Player(trucoPlayer.getId(), trucoPlayer.getName());
    }


    public static TrucoPlayer getPlayer(TrucoUser trucoUser) {
        if (trucoUser == null)
            return null;

        return new TrucoPlayer(trucoUser.getId());
    }

    public static TrucoCard getCard(SpanishCard card) {
        if (card == null)
            return null;

        if (SpanishCard.ESPADA.equals(card.getType())) {
            return new TrucoCard(TrucoCard.ESPADA, card.getValue());
        } else if (SpanishCard.ORO.equals(card.getType())) {
            return new TrucoCard(TrucoCard.ORO, card.getValue());
        } else if (SpanishCard.COPA.equals(card.getType())) {
            return new TrucoCard(TrucoCard.COPA, card.getValue());
        } else if (SpanishCard.BASTO.equals(card.getType())) {
            return new TrucoCard(TrucoCard.BASTO, card.getValue());
        }
        return null;
    }

    public static TrucoCard[] getCards(List<SpanishCard> cards) {
        if (cards == null)
            return null;

        TrucoCard[] trucoCards = new TrucoCard[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            trucoCards[i] = getCard(cards.get(i));
        }
        return trucoCards;

    }

    public static TrucoEvent trucoEvent(TrucoGameEvent trucoGameEvent) {
        TrucoEvent trucoEvent = new TrucoEvent();
        trucoEvent.setTypeEvent(trucoEventType(trucoGameEvent));
        trucoEvent.setType(trucoEventType(trucoGameEvent));
        trucoEvent.setCard(getCard(trucoGameEvent.getCard()));
        trucoEvent.setCards(getCards(trucoGameEvent.getCards()));
        trucoEvent.setPlayer(getPlayer(trucoGameEvent.getPlayer()));
        trucoEvent.setTableNumber(Integer.parseInt(trucoGameEvent.getGame().getId()));
        trucoEvent.setHand(trucoGameEvent.getGame().getHandNumber());
        return trucoEvent;

    }

    public static byte trucoEventType(TrucoGameEvent trucoGameEvent) {
        if (Event.PLAY_CARD.equalsIgnoreCase(trucoGameEvent.getEventName()))
            return TrucoEvent.JUGAR_CARTA;

        if (Event.HAND_STARTED.equalsIgnoreCase(trucoGameEvent.getEventName()))
            return TrucoEvent.INICIO_DE_MANO;

        if (Event.GIVING_CARDS.equalsIgnoreCase(trucoGameEvent.getEventName()))
            return TrucoEvent.ENVIAR_CARTAS;

        if (Event.TRUCO_GAME_EVENT.equalsIgnoreCase(trucoGameEvent.getEventName()))
            return TrucoEvent.INICIO_DE_JUEGO;

        if (Event.PLAY_REQUEST.equalsIgnoreCase(trucoGameEvent.getEventName())) {
//            if (Event.PLAY_REQUEST_CARD.equalsIgnoreCase(trucoGameEvent.getRequest())) {
//                return TrucoEvent.TURNO_JUGAR_CARTA;
//            }
//            if (Event.PLAY_REQUEST_ENVIDO.equalsIgnoreCase(trucoGameEvent.getRequest())) {
//                return TrucoEvent.TURNO_CANTAR_ENVIDO;
//            }
//            if (Event.PLAY_REQUEST_ENVIDO_VALUE.equalsIgnoreCase(trucoGameEvent.getRequest())) {
//                return TrucoEvent.TURNO_RESPONDER_ENVIDO;
//            }
//            if (Event.PLAY_REQUEST_TRUCO.equalsIgnoreCase(trucoGameEvent.getRequest())) {
//                return TrucoEvent.TURNO_RESPONDER_TRUCO;
//            }
        }

        if (Event.GAME_STARTED.equalsIgnoreCase(trucoGameEvent.getEventName())) {
            return TrucoEvent.INICIO_DE_JUEGO;
        }


        throw new RuntimeException("type [" + trucoGameEvent.getEventName() + "] is not available");


    }

    public static String trucoGameEventType(TrucoEvent event) {
        Integer type = (int) event.getType();
        if (type == TrucoEvent.TURNO_JUGAR_CARTA) {
            return Event.PLAY_REQUEST_CARD;
        } else if (type == TrucoEvent.TURNO_CANTAR_ENVIDO) {
            return Event.PLAY_REQUEST_ENVIDO_VALUE;
        } else if (type == TrucoEvent.TURNO_CANTAR_FLOR) {
            return Event.PLAY_REQUEST_FLOR_VALUE;
        } else if (type == TrucoEvent.TURNO_RESPONDER_TRUCO) {
            return Event.PLAY_REQUEST_TRUCO;
        } else if (type == TrucoEvent.TURNO_RESPONDER_RETRUCO) {
            return Event.PLAY_REQUEST_RETRUCO;
        } else if (type == TrucoEvent.TURNO_RESPONDER_VALECUATRO) {
            return Event.PLAY_REQUEST_VALE_CUATRO;
        } else if (type == TrucoEvent.TURNO_CANTAR_ENVIDO) {
            return Event.PLAY_REQUEST_ENVIDO;
        } else if (type == TrucoEvent.TURNO_RESPONDER_FALTAENVIDO) {
            return Event.PLAY_REQUEST_FALTA_ENVIDO;
        } else if (type == TrucoEvent.TURNO_RESPONDER_REALENVIDO) {
            return Event.PLAY_REQUEST_REAL_ENVIDO;
        } else if (type == TrucoEvent.TURNO_CANTAR_FLOR) {
            return Event.PLAY_REQUEST_FLOR;
        } else if (type == TrucoEvent.TURNO_RESPONDER_CONTRAFLOR) {
            return Event.PLAY_REQUEST_FLOR;
        } else if (type == TrucoEvent.TURNO_RESPONDER_CONTRAFLORALRESTO) {
            return Event.PLAY_REQUEST_CONTRAFLOR;
        }
        throw new IllegalArgumentException("Play Type is not valid [" + type + "]");
    }

    public static TrucoGameEvent trucoEvent(TrucoEvent event) {
        TrucoGameEvent trucoGameEvent = new TrucoGameEvent();
        trucoGameEvent.setPlayer(getPlayer(event.getPlayer()));

        if (event.getType() == TrucoEvent.JUGAR_CARTA) {
            trucoGameEvent.setEventName(TrucoGamePlay.PLAY_CARD);
            trucoGameEvent.setCard(getSpanishCard(event.getCard()));
        }
        else if (event.getType() == TrucoEvent.PLAYER_CONFIRMADO){
            trucoGameEvent.setEventName(TrucoGamePlay.PLAYER_READY);
            trucoGameEvent.setPlayer(getPlayer(event.getPlayer()));
        }
        else if (event.getType() == TrucoEvent.TRUCO){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_TRUCO);
            trucoGameEvent.setText("Truco");
        }
        else if (event.getType() == TrucoEvent.RETRUCO){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_RETRUCO);
            trucoGameEvent.setText("Quiero ReTruco");
        }
        else if (event.getType() == TrucoEvent.VALE_CUATRO){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_VALECUATRO);
            trucoGameEvent.setText("Quiero Vale Cuatro");
        }
        else if (event.getType() == TrucoEvent.QUIERO){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_QUIERO);
            trucoGameEvent.setText("Quiero");
        }
        else if (event.getType() == TrucoEvent.NO_QUIERO){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_NO_QUIERO);
            trucoGameEvent.setText("No Quiero");
        }
        else if (event.getType() == TrucoEvent.ENVIDO){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_ENVIDO);
            trucoGameEvent.setText("Envido");
        }
        else if (event.getType() == TrucoEvent.REAL_ENVIDO){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_REAL_ENVIDO);
            trucoGameEvent.setText("Real Envido");
        }
        else if (event.getType() == TrucoEvent.FALTA_ENVIDO){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_FALTA_ENVIDO);
            trucoGameEvent.setText("Falta Envido");
        }
        else if (event.getType() == TrucoEvent.CANTO_ENVIDO){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_ENVIDO_VALUE);
            trucoGameEvent.setText(Integer.toString(event.getValue()));
        }
        else if (event.getType() == TrucoEvent.PASO_ENVIDO){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_PASO_ENVIDO);
            trucoGameEvent.setText("Paso");
        }
        else if (event.getType() == TrucoEvent.PASO_FLOR){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_PASO_FLOR);
            trucoGameEvent.setText("Paso");
        }
        else if (event.getType() == TrucoEvent.FLOR){
            trucoGameEvent.setEventName(TrucoGamePlay.SAY_FLOR);
            trucoGameEvent.setText("Flor");
        }
        else if (event.getType() == TrucoEvent.ME_VOY_AL_MAZO){
            trucoGameEvent.setEventName(TrucoGamePlay.PLAY_ME_VOY_AL_MAZO);
            trucoGameEvent.setText("Me voy al Mazo");
        }
        else {
            throw new IllegalArgumentException("Event [" + event + "] not found");
        }

        return trucoGameEvent;

    }


    public static TrucoGamePlay getPlay(TrucoPlay trucoPlay) {
        TrucoGamePlay trucoGamePlay = new TrucoGamePlay();
        if (TrucoPlay.JUGAR_CARTA == trucoPlay.getType()) {
            trucoGamePlay.setType(TrucoGamePlay.PLAY_CARD);
            trucoGamePlay.setCard(getSpanishCard(trucoPlay.getCard()));
        }
        return trucoGamePlay;
    }

    public static TrucoPlay getPlay(TrucoGamePlay trucoGamePlay) {
        TrucoPlay trucoPlay = new TrucoPlay();
        trucoPlay.setPlayer(getPlayer(trucoGamePlay.getPlayer()));

        if (TrucoGamePlay.PLAY_CARD.equalsIgnoreCase(trucoGamePlay.getType())) {
            trucoPlay.setType(TrucoPlay.JUGAR_CARTA);
            trucoPlay.setCard(getCard(trucoGamePlay.getCard()));
        }
        if (TrucoGamePlay.SAY_ENVIDO.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.ENVIDO);
        }
        if (TrucoGamePlay.SAY_REAL_ENVIDO.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.REAL_ENVIDO);
        }
        if (TrucoGamePlay.SAY_FALTA_ENVIDO.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.FALTA_ENVIDO);
        }
        if (TrucoGamePlay.SAY_TRUCO.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.TRUCO);
        }
        if (TrucoGamePlay.SAY_RETRUCO.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.RETRUCO);
        }
        if (TrucoGamePlay.SAY_VALECUATRO.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.VALE_CUATRO);
        }
        if (TrucoGamePlay.SAY_QUIERO.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.QUIERO);
        }

        if (TrucoGamePlay.SAY_QUIERO_PLUS.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.QUIERO);
        }

        if (TrucoGamePlay.SAY_NO_QUIERO.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.NO_QUIERO);
        }
        if (TrucoGamePlay.PLAY_ME_VOY_AL_MAZO.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.ME_VOY_AL_MAZO);
        }
        if (TrucoGamePlay.SAY_PASO_ENVIDO.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.PASO_ENVIDO);
        }
        if (TrucoGamePlay.SAY_PASO_FLOR.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.PASO_FLOR);
        }
        if (TrucoGamePlay.CLOSE_CARDS.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.CERRARSE);
        }
        if (TrucoGamePlay.SAY_ENVIDO_VALUE.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.CANTO_ENVIDO);
            trucoPlay.setValue(trucoGamePlay.getEnvido());
        }
        if (TrucoGamePlay.SAY_FLOR.equalsIgnoreCase(trucoGamePlay.getType())){
            trucoPlay.setType(TrucoPlay.FLOR);
        }

        return trucoPlay;
    }

    public static List<SpanishCard> getSpanishCards(TrucoCard[] trucoCards) {
        List spanishCards = new ArrayList();
        for (TrucoCard trucoCard : trucoCards) {
            if (trucoCard != null) {
                SpanishCard spanishCard = getSpanishCard(trucoCard);
                spanishCards.add(spanishCard);
            }
        }
        return spanishCards;
    }

    public static SpanishCard getSpanishCard(TrucoCard trucoCard) {
        SpanishCard spanishCard = new SpanishCard();
        spanishCard.setType(getType(trucoCard));
        spanishCard.setValue(getValue(trucoCard));
        return spanishCard;
    }


    static String getType(TrucoCard trucoCard) {
        byte type = trucoCard.getKind();
        if (TrucoCard.ORO == type)
            return SpanishCard.ORO;
        if (TrucoCard.COPA == type)
            return SpanishCard.COPA;
        if (TrucoCard.ESPADA == type)
            return SpanishCard.ESPADA;
        if (TrucoCard.BASTO == type)
            return SpanishCard.BASTO;

        return null;
    }

    static Integer getValue(TrucoCard trucoCard) {
        return (int) trucoCard.getValue();
    }


}
