package br.com.livelo.orderflight.service.reservation;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.ReservationResponse;

public interface ReservationService {
    ReservationResponse createOrder(ReservationRequest request, String transactionId, String customerId,
                                    String channel,
                                    String listPrice, String userId);
}
