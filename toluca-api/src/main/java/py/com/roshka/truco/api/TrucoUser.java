package py.com.roshka.truco.api;

import java.util.Objects;

public class TrucoUser {
    private String id;
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrucoUser trucoUser = (TrucoUser) o;
        return Objects.equals(id, trucoUser.id) &&
                Objects.equals(username, trucoUser.username);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, username);
    }
}
