package py.com.roshka.truco.api;

import java.util.Objects;

public class TrucoGameData {
    String id;
    private Integer points;
    private TrucoGameTeam team1;
    private TrucoGameTeam team2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public TrucoGameTeam getTeam1() {
        return team1;
    }

    public void setTeam1(TrucoGameTeam team1) {
        this.team1 = team1;
    }

    public TrucoGameTeam getTeam2() {
        return team2;
    }

    public void setTeam2(TrucoGameTeam team2) {
        this.team2 = team2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrucoGameData trucoGame = (TrucoGameData) o;
        return Objects.equals(id, trucoGame.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TrucoGame{" +
                "id='" + id + '\'' +
                ", points=" + points +
                ", team1=" + team1 +
                ", team2=" + team2 +
                '}';
    }
}
