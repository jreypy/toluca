package py.com.roshka.truco.ui.event;

import py.com.roshka.truco.api.TrucoEvent;

public class TrucoPlay2 {

    py.com.roshka.truco.api.TrucoEvent source;

    public TrucoPlay2(py.com.roshka.truco.api.TrucoEvent source) {
        this.source = source;
    }

    public TrucoEvent getSource() {
        return source;
    }
}
