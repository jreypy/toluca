package py.com.roshka.toluca.websocket.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class AMQPConfiguration implements RabbitListenerConfigurer {


    @Bean
    public TopicExchange topicExchange1(@Value("truco_event") final String topicName) {
        return new TopicExchange(topicName, false, false);
    }

    @Bean
    public TopicExchange topicExchange2(@Value("truco_room_event") final String topicName) {
        return new TopicExchange(topicName, false, false);
    }

    @Bean
    public TopicExchange trucoGameTopic(@Value("truco_game_event") final String topicName) {
        return new TopicExchange(topicName, false, false);
    }

    @Bean
    public Queue queue1(@Value("truco_client") final String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue queue2(@Value("truco_room_client") final String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue trucoGameClient(@Value("truco_game_client") final String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding binding1(final Queue queue1, final TopicExchange topicExchange1) {
        return BindingBuilder
                .bind(queue1).to(topicExchange1).with("*");
    }

    @Bean
    public Binding binding2(final Queue queue2, final TopicExchange topicExchange2) {
        return BindingBuilder
                .bind(queue2).to(topicExchange2).with("*");
    }

    @Bean
    public Binding trucoGameBinding(final Queue trucoGameClient, final TopicExchange trucoGameTopic) {
        return BindingBuilder
                .bind(trucoGameClient).to(trucoGameTopic).with("*");
    }

//    @Bean
//    public SimpleMessageListenerContainer simpleRabbitListener1(
//            final ConnectionFactory connectionFactory,
//            final Queue queue1
//    ) {
//        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
//        listenerContainer.addQueues(queue1);
//        listenerContainer.setMessageConverter(new Jackson2JsonMessageConverter());
//
//        return listenerContainer;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer simpleRabbitListener2(
//            final ConnectionFactory connectionFactory,
//            final Queue queue2
//    ) {
//        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
//        listenerContainer.addQueues(queue2);
//        listenerContainer.setMessageListener(
//                message -> {
//                    System.out.println("Message simple listener 1 [" + message + "]");
//                }
//        );
//        listenerContainer.setMessageConverter(new Jackson2JsonMessageConverter());
//        return listenerContainer;
//    }


    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}
