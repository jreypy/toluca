package py.com.roshka.toluca.websocket.beans;

public class CommandResponse {
    public static final String COMMAND_RESPONSE = "COMMAND_RESPONSE";
    final private String type = COMMAND_RESPONSE;
    private String command;
    private String commandId;
    private Object data;

    public CommandResponse(String command, String commandId, Object data) {
        this.command = command;
        this.commandId = commandId;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
