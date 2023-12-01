package br.com.livelo.orderflight.services;

public interface RabbitMQService {
    void sendMessage(ProductDTO product);
}
