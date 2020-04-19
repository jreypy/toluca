package py.com.roshka.truco.ui.event;

import py.com.roshka.truco.api.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TableEvent;

public class TableEvent2 extends TableEvent {
    TrucoEvent source;

    public TableEvent2(TrucoEvent source) {
        this.source = source;
    }

    public TrucoEvent getSource() {
        return source;
    }


}
