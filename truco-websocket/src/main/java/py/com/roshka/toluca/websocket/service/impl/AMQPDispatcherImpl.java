package py.com.roshka.toluca.websocket.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.service.AMQPDispatcher;
import py.com.roshka.truco.api.RabbitResponse;
import py.com.roshka.truco.api.TrucoEvent;


import java.util.HashMap;

@Component
public class AMQPDispatcherImpl implements AMQPDispatcher {

    Logger logger = LoggerFactory.getLogger(AMQPDispatcherImpl.class);

    private RabbitTemplate rabbitTemplate;
    private ObjectMapper objectMapper;

    public AMQPDispatcherImpl(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(String topic, String routingKey, String eventName, TrucoEvent trucoEvent) {
        logger.debug("Send data to [" + topic + "][" + routingKey + "]");
        RabbitResponse rabbitResponse = new RabbitResponse(eventName, trucoEvent);

        rabbitTemplate.convertAndSend(
                topic,
                routingKey,
                rabbitResponse);
    }

}
