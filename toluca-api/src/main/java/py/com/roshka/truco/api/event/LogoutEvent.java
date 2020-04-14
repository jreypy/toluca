package py.com.roshka.truco.api.event;

import py.com.roshka.truco.api.TrucoEvent;
import py.com.roshka.truco.api.TrucoUser;

public class LogoutEvent extends TrucoEvent {
    private TrucoUser trucoUser;
    public LogoutEvent() {
    }

    public LogoutEvent(String message, TrucoUser trucoUser) {
        super(message);
        this.trucoUser = trucoUser;
    }


    public TrucoUser getTrucoUser() {
        return trucoUser;
    }

    public void setTrucoUser(TrucoUser trucoUser) {
        this.trucoUser = trucoUser;
    }
}
