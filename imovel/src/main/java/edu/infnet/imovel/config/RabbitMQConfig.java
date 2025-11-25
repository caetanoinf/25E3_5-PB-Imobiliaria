package edu.infnet.imovel.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "imovel.exchange";
    public static final String QUEUE_NAME = "imovel.criado.queue";
    public static final String ROUTING_KEY = "imovel.criado";

    @Bean
    public TopicExchange imovelExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue imovelCriadoQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding imovelCriadoBinding(Queue imovelCriadoQueue, TopicExchange imovelExchange) {
        return BindingBuilder
                .bind(imovelCriadoQueue)
                .to(imovelExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
