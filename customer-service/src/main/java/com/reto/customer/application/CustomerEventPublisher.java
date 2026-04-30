package com.reto.customer.application;

public interface CustomerEventPublisher {

    void publish(ClienteChangedEvent event);
}
