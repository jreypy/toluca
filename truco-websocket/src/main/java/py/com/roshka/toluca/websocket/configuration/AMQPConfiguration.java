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

    /**
     * Eventos Genericos
     *
     * @param topicName
     * @return
     */

    static String ROOM_ALL_ROUTING_KEY = "room_all";
    static String ROOM_ID_ROUTING_KEY = "room_id";
    static String ROOM_JOIN_ROUTING_KEY = "room_join";
    static String ROOM_LOGOUT_ROUTING_KEY = "room_logout";


    /**
     * Eventos de Room
     *
     * @param topicName
     * @return
     */

    @Bean
    public TopicExchange trucoRoomTopic(@Value("truco_room_event") final String topicName) {
        return new TopicExchange(topicName, false, false);
    }

    /**
     * Queue para todos
     *
     * @param queueName
     * @return
     */
    @Bean
    public Queue trucoRoomQueue(@Value("truco_room_all_client") final String queueName) {
        return new Queue(queueName, true);
    }

    /**
     * Queue para un Room Especifico
     *
     * @param queueName
     * @return
     */
    @Bean
    public Queue trucoRoomIdQueue(@Value("truco_room_id_client") final String queueName) {
        return new Queue(queueName, true);
    }

    /**
     * Queue para JOIN de Room
     *
     * @param queueName
     * @return
     */
    @Bean
    public Queue trucoRoomJoinQueue(@Value("truco_room_join_client") final String queueName) {
        return new Queue(queueName, true);
    }

    /**
     * Binding para todos los Rooms
     *
     * @return
     */
    @Bean
    public Binding trucoRoomBinding(final Queue trucoRoomQueue, final TopicExchange trucoRoomTopic) {
        return BindingBuilder
                .bind(trucoRoomQueue).to(trucoRoomTopic).with(ROOM_ALL_ROUTING_KEY);
    }

    /**
     * Binding para un Room en Especifico
     *
     * @return
     */
    @Bean
    public Binding trucoRoomIdBinding(final Queue trucoRoomIdQueue, final TopicExchange trucoRoomTopic) {
        return BindingBuilder
                .bind(trucoRoomIdQueue).to(trucoRoomTopic).with(ROOM_ID_ROUTING_KEY);
    }

    /**
     * Binding para J
     *
     * @return
     */
    @Bean
    public Binding trucoRoomJoinBinding(final Queue trucoRoomJoinQueue, final TopicExchange trucoRoomTopic) {
        return BindingBuilder
                .bind(trucoRoomJoinQueue).to(trucoRoomTopic).with(ROOM_JOIN_ROUTING_KEY);
    }


    /**
     * Eventos para Table
     */

    @Bean
    public TopicExchange trucoTableTopic(@Value("truco_table_event") final String topicName) {
        return new TopicExchange(topicName, false, false);
    }

    @Bean
    public Queue trucoTableQueue(@Value("truco_table_client") final String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding trucoTableBinding(final Queue trucoTableQueue, final TopicExchange trucoTableTopic) {
        return BindingBuilder
                .bind(trucoTableQueue).to(trucoTableTopic).with("*");
    }

    /**
     *
     * Eventos para TrucoGame
     */
    @Bean
    public TopicExchange trucoGameTopic(@Value("truco_game_event") final String topicName) {
        return new TopicExchange(topicName, false, false);
    }

    @Bean
    public Queue trucoGameQueue(@Value("truco_game_client") final String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding trucoGameBinding(final Queue trucoGameQueue, final TopicExchange trucoGameTopic) {
        return BindingBuilder
                .bind(trucoGameQueue).to(trucoGameTopic).with("*");
    }

    //Direct Message
    @Bean
    public TopicExchange directMessageTopic(@Value("direct_message") final String directMessage) {
        return new TopicExchange(directMessage, false, false);
    }
    @Bean
    public Queue directMessageQueue(@Value("direct_message_queue") final String queueName) {
        return new Queue(queueName, true);
    }
    @Bean
    public Binding directMessageBinding(final Queue directMessageQueue, final TopicExchange directMessageTopic) {
        return BindingBuilder
                .bind(directMessageQueue).to(directMessageTopic).with("*");
    }


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
