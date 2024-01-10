package br.com.livelo.orderflight.service.cart;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.livelo.orderflight.domain.dto.CartRequest;
import br.com.livelo.orderflight.domain.dto.CartResponse;
import br.com.livelo.orderflight.domain.mappers.CartRequestMapper;
import br.com.livelo.orderflight.domain.mappers.OrderEntityMapper;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.PartnerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final OrderRepository orderRepository;
    
    private final PartnerService partnerService;
    
    private final CartRequestMapper cartRequestMapper;
    
    private final OrderEntityMapper orderEntityMapper;

    public CartResponse createOrder(CartRequest request, String transacationId, String customerId, String channel) {
        var partnerReservationResponse = partnerService.reservation(cartRequestMapper.toPartnerReservationRequest(request), transacationId);
        //var order = this.orderRepository.save(cartRequestMapper.toOrderEntity(request, transacationId, customerId, channel, partnerReservationResponse));
        
        var order = cartRequestMapper.toOrderEntity(request, transacationId, customerId, channel, partnerReservationResponse);
        return orderEntityMapper.toCartResponse(order);
    }
}
