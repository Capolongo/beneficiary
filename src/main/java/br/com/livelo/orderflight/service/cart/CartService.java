package br.com.livelo.orderflight.service.cart;

import br.com.livelo.orderflight.domain.dto.CartRequest;
import br.com.livelo.orderflight.domain.dto.CartResponse;
import br.com.livelo.orderflight.domain.dto.PartnerReservationItem;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.domain.mappers.CartItemMapper;
import br.com.livelo.orderflight.domain.mappers.CartMapper;
import br.com.livelo.orderflight.domain.mappers.CartRequestMapper;
import br.com.livelo.orderflight.domain.mappers.OrderEntityMapper;
import br.com.livelo.orderflight.exception.CartException;
import br.com.livelo.orderflight.exception.enuns.CartErrorType;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.PartnerService;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final OrderRepository orderRepository;

    private final PartnerService partnerService;

    private final CartRequestMapper cartRequestMapper;

    private final OrderEntityMapper orderEntityMapper;

    private final CartMapper cartMapper;


    public CartResponse createOrder(CartRequest request, String transactionId, String customerId, String channel) {
        try {
            var partnerReservationResponse = partnerService.reservation(cartRequestMapper.toPartnerReservationRequest(request), transactionId);

            OrderEntity orderEntity = cartMapper.toOrderEntity(request, partnerReservationResponse, transactionId, customerId, channel);
            //TODO perguntar o que passar no priceListId
            orderEntity.getPrice().setPriceListId("p123");
            orderRepository.save(orderEntity);

            return new CartResponse("", "", "", LocalDateTime.now(), "", "", "", "", "", LocalDateTime.now());
        } catch (CartException e) {
            throw e;
        } catch (PersistenceException e) {
            //TODO TRATAR EXCEÇÕES DE SQL
            throw new CartException(CartErrorType.INTERNAL_ERROR, e.getMessage(), null, e);
        } catch (Exception e) {
            e.printStackTrace();
            //TODO TRATAR EXCEÇÕES DE MAPPER
            throw e;
        }
    }
}
