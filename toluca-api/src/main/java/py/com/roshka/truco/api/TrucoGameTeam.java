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


    private Integer points = 0;


    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "TrucoGameTeam{" +
                "players=" + players +
                '}';
    }
}
