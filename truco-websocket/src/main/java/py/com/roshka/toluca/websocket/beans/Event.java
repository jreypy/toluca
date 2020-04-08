package py.com.roshka.toluca.websocket.beans;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Event {
    private String type;
    private Map data;


    public Event(String type, Map data) {
        this.type = type;
        this.data = data;
    }

    public Event(String type, List list) {
        this.type = type;
        list(list);
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    public void list(Object list) {
        Map map = new LinkedHashMap();
        map.put("items", list);
        this.data = map;
    }
}
