package br.com.livelo.orderflight.service.reservation;

import br.com.livelo.orderflight.config.PartnerProperties;
import br.com.livelo.orderflight.domain.dto.reservation.request.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationResponse;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.proxy.PartnerConnectorProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.livelo.orderflight.exception.enuns.ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR;


@Service
@RequiredArgsConstructor
public class RetrieveReservationService {
    private final PartnerConnectorProxy partnerConnectorProxy;
    private final PartnerProperties partnerProperties;

    public PartnerReservationResponse reservationUsingRetry(PartnerReservationRequest request, String transactionId, Optional<Integer> attempt) {
        int currentAttempt = attempt.orElseGet(() -> {
            Integer attemptValueResult = partnerProperties.getAttemptByPartnerCode(request.getPartnerCode());
            return attemptValueResult != null ? attemptValueResult : 0;
        });

        try {
            return this.partnerConnectorProxy.reservation(request, transactionId);
        } catch (ReservationException reservationException) {
            if (reservationException.getReservationErrorType().getCode().equals(FLIGHT_CONNECTOR_INTERNAL_ERROR.getCode()) && currentAttempt > 0) {

                return reservationUsingRetry(request, transactionId, Optional.of(currentAttempt - 1));
            }
            throw reservationException;
        }
    }
}
