package br.com.livelo.orderflight.service.cart;

import br.com.livelo.orderflight.domain.dto.CartRequest;
import br.com.livelo.orderflight.domain.dto.CartResponse;
import br.com.livelo.orderflight.domain.mappers.CartRequestMapper;
import br.com.livelo.orderflight.domain.mappers.OrderEntityMapper;
import br.com.livelo.orderflight.exception.CartException;
import br.com.livelo.orderflight.exception.enuns.CartErrorType;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.PartnerService;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final OrderRepository orderRepository;

    private final PartnerService partnerService;

    private final CartRequestMapper cartRequestMapper;

    private final OrderEntityMapper orderEntityMapper;

    public CartResponse createOrder(CartRequest request, String transacationId, String customerId, String channel) {
        try {
            var partnerReservationResponse = partnerService.reservation(cartRequestMapper.toPartnerReservationRequest(request), transacationId);
            //TODO UTILIZAR O MAPSTRUCT
            var order = cartRequestMapper.toOrderEntity(request, transacationId, customerId, channel, partnerReservationResponse);
            //TODO REALIZAR A PERSISTENCIA DA ORDER COM SUCESSO
            this.orderRepository.save(cartRequestMapper.toOrderEntity(request, transacationId, customerId, channel, partnerReservationResponse));
            //TODO UTILIZAR MAPSTRUCT (BRUNO e RENAN)
            return orderEntityMapper.toCartResponse(order);
        } catch (CartException e) {
            throw e;
        } catch (PersistenceException e) {
            //TODO TRATAR EXCEÇÕES DE SQL
            throw new CartException(CartErrorType.INTERNAL_ERROR, e.getMessage(), null, e);
        } catch (Exception e) {
            //TODO TRATAR EXCEÇÕES DE MAPPER
            throw e;
        }
    }
}
