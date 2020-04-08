package py.com.roshka.toluca.websocket.service;

import py.com.roshka.toluca.websocket.beans.Event;

public interface EventProcessor {

    Event sendEvent(String eventType, Object data);


}
