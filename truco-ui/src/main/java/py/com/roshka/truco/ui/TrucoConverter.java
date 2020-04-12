package py.com.roshka.truco.ui;

import py.com.roshka.truco.api.Player;
import py.com.roshka.truco.api.SpanishCard;
import py.com.roshka.truco.api.TrucoGameTeam;
import py.edu.uca.fcyt.toluca.game.TrucoCard;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;

import java.util.ArrayList;
import java.util.List;

public class TrucoConverter {
    static TrucoTeam getTeam(TrucoGameTeam trucoGameTeam) {
        TrucoTeam trucoTeam = new TrucoTeam();
        trucoGameTeam.setPlayers(new ArrayList<>());
        for (Player player : trucoGameTeam.getPlayers()) {
            trucoTeam.getPlayers().add(getPlayer(player));
        }
        return trucoTeam;
    }


    static TrucoPlayer getPlayer(Player player) {
        return new TrucoPlayer(player.getId());
    }

    public static TrucoCard getCard(SpanishCard card) {
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
        TrucoCard[] trucoCards = new TrucoCard[cards.size()];
        for (int i=0; i< cards.size(); i++ ){
            trucoCards[i] = getCard(cards.get(i));
        }
        return  trucoCards;

    }
}
