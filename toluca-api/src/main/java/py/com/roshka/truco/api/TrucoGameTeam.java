package py.com.roshka.truco.api;

import java.util.ArrayList;
import java.util.List;

public class TrucoGameTeam {
    private List<Player> players = new ArrayList<>();

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
