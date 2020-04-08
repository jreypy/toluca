package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import py.com.roshka.toluca.websocket.beans.Command;
import py.com.roshka.toluca.websocket.beans.Event;

import java.util.List;
import java.util.Map;

public class Processor {

    protected ObjectMapper objectMapper;

    public Processor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Event getEvent(String eventName, Object object) {
        return new Event(eventName, objectMapper.convertValue(object, Map.class));
    }

    public Event getEvent(String eventName, List list) {
        return new Event(eventName, list);
    }

    public Event getEvent(Command command, List list) {
        return getEvent(command.getCommand(), list);
    }

    public Event getEvent(Command command, Object object) {
        return getEvent(command.getCommand(), object);
    }
}
