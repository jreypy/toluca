package py.com.roshka.truco.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.Event;
import py.com.roshka.truco.api.RabbitRequest;
import py.com.roshka.truco.api.RabbitResponse;
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
    void receiveTrucoServerQueue(final RabbitRequest rabbitRequest) {
        logger.debug("Receiving [" + rabbitRequest + "]");
        if (rabbitRequest.getType().equalsIgnoreCase(RabbitResponse.class.getCanonicalName())) {
            RabbitResponse rabbitResponse = objectMapper.convertValue(rabbitRequest.getData(), RabbitResponse.class);
            if (Event.LOGOUT.equalsIgnoreCase(rabbitResponse.getEventName())) {
                trucoRoomSvc.logout(((String) (rabbitResponse.getData().get("username"))));
            }
        }
    }
}
