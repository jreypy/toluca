package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.beans.Event;
import py.com.roshka.toluca.websocket.service.EventProcessor;

@Component
public class EventProcessorImpl extends Processor implements EventProcessor {

    public EventProcessorImpl(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Event sendEvent(String eventName, Object data) {
        return getEvent(eventName, data);
    }
}
