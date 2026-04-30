package com.reto.account.infrastructure;

import com.reto.account.application.ClienteChangedEvent;
import com.reto.account.application.ClienteReplicaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClienteEventListener {

    private final ClienteReplicaService service;

    public ClienteEventListener(ClienteReplicaService service) {
        this.service = service;
    }

    @RabbitListener(queues = AmqpConfig.QUEUE, autoStartup = "${app.rabbit.listeners-enabled:false}")
    public void handle(ClienteChangedEvent event) {
        service.sync(event);
    }
}
