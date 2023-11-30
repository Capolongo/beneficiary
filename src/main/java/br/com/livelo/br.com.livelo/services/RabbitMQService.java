package br.com.livelo.br.com.livelo.services;

import br.com.livelo.br.com.livelo.dto.ProductDTO;

public interface RabbitMQService {

    void sendMessage(ProductDTO product);

}
