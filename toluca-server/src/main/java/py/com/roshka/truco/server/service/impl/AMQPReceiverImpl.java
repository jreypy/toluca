package py.com.roshka.truco.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.RabbitResponse;
import py.com.roshka.truco.api.event.LogoutEvent;
import py.com.roshka.truco.server.service.AMQPReceiver;
import py.com.roshka.truco.server.service.TrucoRoomSvc;

@Component
public class AMQPReceiverImpl implements AMQPReceiver {
    Logger logger = LoggerFactory.getLogger(AMQPReceiverImpl.class);

    ObjectMapper objectMapper;
    TrucoRoomSvc trucoRoomSvc;

    public AMQPReceiverImpl(ObjectMapper objectMapper, TrucoRoomSvc trucoRoomSvc) {
        this.objectMapper = objectMapper;
        this.trucoRoomSvc = trucoRoomSvc;
    }

    @RabbitListener(queues = "truco_server")
    void receiveTrucoServerQueue(final RabbitResponse rabbitResponse) {
        logger.debug("Receiving [" + rabbitResponse + "]");
        if (Event.LOGOUT.equalsIgnoreCase(rabbitResponse.getEventName())) {
            LogoutEvent logoutEvent = objectMapper.convertValue(rabbitResponse.getData(), LogoutEvent.class);
            if (Event.LOGOUT.equalsIgnoreCase(rabbitResponse.getEventName())) {
                trucoRoomSvc.logout(logoutEvent.getTrucoUser().getUsername());
            }
        }
    }
}
