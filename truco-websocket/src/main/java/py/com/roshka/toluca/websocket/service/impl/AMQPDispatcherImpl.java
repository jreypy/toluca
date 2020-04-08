package py.com.roshka.toluca.websocket.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import py.com.roshka.toluca.websocket.service.AMQPDispatcher;
import py.com.roshka.truco.api.RabbitRequest;

@Component
public class AMQPDispatcherImpl implements AMQPDispatcher {

    Logger logger = LoggerFactory.getLogger(AMQPDispatcherImpl.class);

    private RabbitTemplate rabbitTemplate;

    public AMQPDispatcherImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String topic, String routingKey, Object data) {
        logger.debug("Send data to [" + topic + "][" + routingKey + "]");
        RabbitRequest rabbitRequest = new RabbitRequest(data.getClass().getCanonicalName(), data);

        rabbitTemplate.convertAndSend(
                topic,
                routingKey,
                rabbitRequest);
    }

}
