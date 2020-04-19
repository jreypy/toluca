package py.com.roshka.truco.api;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpanishCard that = (SpanishCard) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    @Override
    public String toString() {
        return "SpanishCard{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
