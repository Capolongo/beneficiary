package br.com.livelo.orderflight.service.cart;

import br.com.livelo.orderflight.domain.dto.CartRequest;
import br.com.livelo.orderflight.domain.dto.CartResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartService {
    private final OrderRepository orderRepository;

    public CartResponse createOrder(CartRequest request) {
        //TODO REQUEST DEVE SER TRANSFORMADO NUM ORDEENTITY
        //TODO FAZER CHAMADA LATERAL AO PARCEIRO PARA REALIZAÇÃO DE BOOKING
        var order = this.orderRepository.save(new OrderEntity());
        //TODO TRANSFORMAR A ORDER EM UM CART RESPONSE
        return new CartResponse("", "", "", LocalDateTime.now(), "", "", "", "", "", LocalDateTime.now());
    }
}
