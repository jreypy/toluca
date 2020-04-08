package py.com.roshka.truco.api;

public class RabbitResponse {
    private String type;
    private Object data;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
