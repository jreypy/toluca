package py.com.roshka.truco.server.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import py.com.roshka.truco.api.RabbitRequest;
import py.com.roshka.truco.server.service.AMQPReceiver;

@Component
public class AMQPReceiverImpl implements AMQPReceiver {
    Logger logger = LoggerFactory.getLogger(AMQPReceiverImpl.class);

    @RabbitListener(queues = "truco_server")
    void handleMultiplicationSolved(final RabbitRequest rabbitRequest) {
        logger.debug("Receiving [" + rabbitRequest + "]");
        System.out.println("Request-> [" + rabbitRequest.getType() + "][" + rabbitRequest.getData() + "]");
    }
}
