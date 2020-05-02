package py.com.roshka.truco.server.beans.holder.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.com.roshka.truco.api.TrucoGameEvent;
import py.com.roshka.truco.api.helper.TolucaHelper;
import py.com.roshka.truco.server.beans.holder.TrucoGameHolder;
import py.edu.uca.fcyt.toluca.event.TrucoEvent;
import py.edu.uca.fcyt.toluca.event.TrucoListener;
import py.edu.uca.fcyt.toluca.game.TrucoGameImpl;
import py.edu.uca.fcyt.toluca.game.TrucoPlayer;
import py.edu.uca.fcyt.toluca.game.TrucoTeam;

public class TrucoGameImpl2 extends TrucoGameImpl {
    Logger logger = LoggerFactory.getLogger(TrucoGameImpl2.class);


    public TrucoGameImpl2(TrucoTeam tm1, TrucoTeam tm2, int points) {
        super(tm1, tm2, points);
    }

    public void fireTurnEvent(TrucoPlayer pl, byte type, int value) { //avisar el Turno con envio del value of envido
        TrucoEvent event1 = new TrucoEvent(this, numberOfHand, pl, type, value); //crear el evento1
        logger.debug("Firing Turn event [" + event1 + "]");
        event1.setTableNumber(getTableNumber());
        for (int i = 0; i < getListenerlist().size(); i++) {
            TrucoListener trucoListener = (TrucoListener) getListenerlist().get(i);
            trucoListener.turn(event1);
        }
    }

    @Override
    public void firePlayResponseEvent(TrucoPlayer pl, byte type, int value) {
        TrucoEvent event = new TrucoEvent(this, numberOfHand, pl, type, value);
        firePlayResponseEvent(event);
    }

    protected void firePlayResponseEvent(TrucoEvent event) {
        logger.debug("Firing Play Response event [" + event + "]");
        if (TrucoEvent.CANTO_ENVIDO == event.getType()) {
            firePlayEvent(event);
        } else {
            logger.warn("PlayResponse have to be fixed [" + event + "]");
        }

    }

}
