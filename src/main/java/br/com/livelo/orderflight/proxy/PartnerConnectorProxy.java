package br.com.livelo.orderflight.proxy;

import br.com.livelo.orderflight.client.PartnerConnectorClient;
import br.com.livelo.orderflight.domain.dto.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.PartnerReservationResponse;
import br.com.livelo.orderflight.exception.ReservationException;
import br.com.livelo.orderflight.exception.enuns.ReservationErrorType;
import br.com.livelo.orderflight.client.Constants;
import br.com.livelo.orderflight.config.PartnerProperties;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerConnectorProxy {
    private final PartnerConnectorClient partnerConnectorClient;
    private final PartnerProperties partnerProperties;

    public PartnerReservationResponse reservation(PartnerReservationRequest request, String transactionId) {
        try {
            var response = partnerConnectorClient.reservation(
                    getUrlByPartnerCode(request.getPartnerCode()),
                    request,
                    getHeaders(Collections.singletonMap(Constants.TRANSACTION_ID, transactionId)));
            return this.handleResponse(response);
        } catch (ReservationException e) {
            throw e;
        } catch (FeignException e) {
            var status = HttpStatus.valueOf(e.status());
            if (status.is5xxServerError()) {
                throw new ReservationException(
                        ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR,
                        null,
                        "Erro interno ao se comunicar com parceiro no conector. ResponseBody: " + e.responseBody().toString(),
                        e
                );
            } else {
                throw new ReservationException(
                        ReservationErrorType.FLIGHT_CONNECTOR_BUSINESS_ERROR,
                        null,
                        "Erro interno ao se comunicar com parceiro no conector. ResponseBody: " + e.responseBody().toString(),
                        e
                );
            }
        } catch (Exception e) {
            throw new ReservationException(ReservationErrorType.ORDER_FLIGHT_INTERNAL_ERROR, e.getMessage(), null, e);
        }
    }

    private MultiValueMap<String, String> getHeaders(Map<String, String> mapHeaders) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        mapHeaders.forEach(headers::add);
        return headers;
    }

    public PartnerReservationResponse handleResponse(ResponseEntity<PartnerReservationResponse> response) {
        var body = ofNullable(response.getBody()).map(Object::toString).orElse(null);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else if (response.getStatusCode().is4xxClientError()) {
            throw new ReservationException(ReservationErrorType.FLIGHT_CONNECTOR_BUSINESS_ERROR, null, body);
        } else {
            throw new ReservationException(ReservationErrorType.FLIGHT_CONNECTOR_INTERNAL_ERROR, null, body);
        }
    }

    //TODO BUSCAR DA LIB
    private URI getUrlByPartnerCode(String partnerCode) {
        return URI.create(partnerProperties.getUrlByPartnerCode(partnerCode));
    }
}
