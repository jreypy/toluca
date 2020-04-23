package py.com.roshka.truco.api;

import java.util.Objects;

public class TrucoGameData {
    String id;
    private Integer points;
    private TrucoGameTeam team1;
    private TrucoGameTeam team2;
    private int handNumber;
    private TrucoUser[] positions;
    private int size;

    public TrucoUser[] getPositions() {
        return positions;
    }

    public void setPositions(TrucoUser[] positions) {
        this.positions = positions;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

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

    public int getHandNumber() {
        return handNumber;
    }

    public void setHandNumber(int handNumber) {
        this.handNumber = handNumber;
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
        return "TrucoGameData{" +
                "id='" + id + '\'' +
                ", points=" + points +
                ", team1=" + team1 +
                ", team2=" + team2 +
                ", handNumber=" + handNumber +
                '}';
    }
}
