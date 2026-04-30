package com.reto.customer.infrastructure;

import com.reto.customer.application.ClienteChangedEvent;
import com.reto.customer.application.CustomerEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitCustomerEventPublisher implements CustomerEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitCustomerEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitCustomerEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(ClienteChangedEvent event) {
        try {
            rabbitTemplate.convertAndSend(AmqpConfig.EXCHANGE, AmqpConfig.ROUTING_KEY, event);
        } catch (AmqpException exception) {
            log.warn("No se pudo publicar el evento de cliente {}: {}", event.clienteId(), exception.getMessage());
        }
    }
}
