package py.com.roshka.truco.api;

public class TrucoPrincipal {
    private String authKey;

    public TrucoPrincipal() {

    }

    public TrucoPrincipal(String authKey) {
        this.authKey = authKey;
    }

    public String getAuthKey() {
        return authKey;
    }
}
