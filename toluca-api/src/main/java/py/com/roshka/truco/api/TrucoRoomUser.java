package py.com.roshka.truco.api;

import java.util.Objects;

public class TrucoRoomUser {
    private TrucoUser user;
    private boolean online;

    public TrucoRoomUser() {
    }

    public TrucoRoomUser(TrucoUser user, boolean online) {
        this.user = user;
        this.online = online;
    }

    public TrucoUser getUser() {
        return user;
    }

    public void setUser(TrucoUser user) {
        this.user = user;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrucoRoomUser that = (TrucoRoomUser) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
