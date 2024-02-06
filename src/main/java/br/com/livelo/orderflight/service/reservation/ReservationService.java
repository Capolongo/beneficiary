package br.com.livelo.orderflight.service.reservation;

import java.util.Optional;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;

public interface ReservationService {
  public ReservationResponse createOrder(ReservationRequest request, String transactionId, String customerId,
      String channel,
      String listPrice);

  public boolean isSameOrderItems(ReservationRequest request, Optional<OrderEntity> orderOptional);
}
