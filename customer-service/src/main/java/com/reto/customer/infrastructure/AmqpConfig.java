package com.reto.customer.infrastructure;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    public static final String EXCHANGE = "bank.clientes.exchange";
    public static final String QUEUE = "bank.clientes.queue";
    public static final String ROUTING_KEY = "cliente.changed";

    @Bean
    DirectExchange customerExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue customerQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    Binding customerBinding(Queue customerQueue, DirectExchange customerExchange) {
        return BindingBuilder.bind(customerQueue).to(customerExchange).with(ROUTING_KEY);
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
