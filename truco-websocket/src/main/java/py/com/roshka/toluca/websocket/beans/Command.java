package py.com.roshka.toluca.websocket.beans;

import java.util.Map;

public class Command {
    String command;
    Map data;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }

    
}
