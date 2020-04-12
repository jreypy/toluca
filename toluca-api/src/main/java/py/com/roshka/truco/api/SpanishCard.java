package py.com.roshka.truco.api;

public class SpanishCard {
    public static String BASTO = "BASTO";
    public static String ORO = "ORO";
    public static String COPA = "COPA";
    public static String ESPADA = "ESPADA";

    private String type;
    private Integer value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
