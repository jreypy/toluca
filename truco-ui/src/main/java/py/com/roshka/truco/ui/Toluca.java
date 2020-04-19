package py.com.roshka.truco.ui;

import py.com.roshka.truco.api.Player;
import py.com.roshka.truco.api.SpanishCard;
import py.com.roshka.truco.api.TrucoUser;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.edu.uca.fcyt.toluca.game.TrucoCard;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Toluca {
    static private Map<String, TrucoPlayer> players = new LinkedHashMap<>();

    synchronized static public TrucoPlayer getTrucoPlayer(TrucoUser trucoUser) {
        TrucoPlayer trucoPlayer = players.get(trucoUser.getId());
        if (trucoPlayer == null) {
            trucoPlayer = TolucaHelper.getPlayer(trucoUser);
            players.put(trucoUser.getId(), trucoPlayer);
        }
        return trucoPlayer;
    }

    synchronized static public TrucoPlayer getTrucoPlayer(Player player) {
        TrucoPlayer trucoPlayer = players.get(player.getId());
        if (trucoPlayer == null) {
            trucoPlayer = TolucaHelper.getPlayer(player);
            players.put(player.getId(), trucoPlayer);
        }
        return trucoPlayer;
    }


    static Map<Integer, TrucoCard> cards = new LinkedHashMap<>();

    public static TrucoCard getCard(SpanishCard card) {
        if (card != null) {
            TrucoCard trucoCard = cards.get(card.hashCode());
            if (trucoCard == null) {
                trucoCard = TolucaHelper.getCard(card);
                cards.put(card.hashCode(), trucoCard);
            }
            return trucoCard;
        }
        return null;

    }

    public static TrucoCard[] getCards(List<SpanishCard> cards) {
        TrucoCard[] trucoCards = new TrucoCard[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            trucoCards[i] = getCard(cards.get(i));
        }
        return trucoCards;
    }
}
