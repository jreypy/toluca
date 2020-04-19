package py.com.roshka.truco.api;

public class TrucoPrincipal {
    private String id;
    private String username;
    private String authKey;

    public TrucoPrincipal() {

    }

    public TrucoPrincipal(String username, String authKey) {
        this.username = username;
        this.authKey = authKey;
        this.id = username;
    }

    public TrucoPrincipal(String authKey) {
        if (authKey == null)
            throw new IllegalArgumentException("Authkey is required");
        this.authKey = authKey;
        username = authKey.split("-")[0];
    }


    public String getId() {
        return id;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.id = username;
        this.username = username;
    }
}
