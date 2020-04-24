package py.com.roshka.truco.api.game;

public class TrucoGameRequest {

    private String type;
    private Integer envido;
    private String text;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEnvido() {
        return envido;
    }

    public void setEnvido(Integer envido) {
        this.envido = envido;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
