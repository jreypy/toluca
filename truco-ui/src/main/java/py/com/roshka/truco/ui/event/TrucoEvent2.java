package py.com.roshka.truco.ui.event;

import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.com.roshka.truco.ui.Toluca;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;

public class TrucoEvent2 extends TrucoEvent {
    py.com.roshka.truco.api.TrucoEvent source;

    public TrucoEvent2(py.com.roshka.truco.api.TrucoEvent source) {
        this.source = source;
    }

    public TrucoEvent2(TrucoGameEvent event) {
        source = event;
        if (event.getPlayer() != null) {
            setPlayer(Toluca.getTrucoPlayer(event.getPlayer()));
        }

        if (Event.HAND_STARTED.equalsIgnoreCase(event.getEventName())) {
            setType(TrucoEvent.INICIO_DE_MANO);
        } else if (Event.GIVING_CARDS.equalsIgnoreCase(event.getEventName())) {
            setType(TrucoEvent.ENVIAR_CARTAS);
        } else if (Event.PLAY_CARD.equalsIgnoreCase(event.getEventName())) {
            setType(TrucoEvent.JUGAR_CARTA);
        } else if (Event.PLAY_REQUEST.equalsIgnoreCase(event.getEventName())) {
            if (getPlayer() == null) {
                throw new IllegalArgumentException("Player is required in PlayRequest Event");
            }
            if (Event.PLAY_REQUEST_CARD.equalsIgnoreCase(event.getRequest())) {
                setType(TrucoEvent.TURNO_JUGAR_CARTA);
            } else {
                throw new IllegalArgumentException("Request not available [" + event.getEventName() + "]");
            }
        } else if (Event.HAND_ENDED.equalsIgnoreCase(event.getEventName())) {
            setType(TrucoEvent.FIN_DE_MANO);
        } else {
            throw new IllegalArgumentException("Event not available [" + event.getEventName() + "]");
        }

    }

    public py.com.roshka.truco.api.TrucoEvent getSource() {
        return source;
    }


}
