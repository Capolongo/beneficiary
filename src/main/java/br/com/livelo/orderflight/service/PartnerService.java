package br.com.livelo.orderflight.service;

import br.com.livelo.orderflight.client.PartnerClient;
import br.com.livelo.orderflight.domain.dto.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.PartnerReservationResponse;
import br.com.livelo.orderflight.exception.CartException;
import br.com.livelo.orderflight.exception.enuns.CartErrorType;
import br.com.livelo.orderflight.utils.Constants;
import br.com.livelo.orderflight.utils.PartnerProperties;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerService {
    private final PartnerClient partnerClient;
    private final PartnerProperties partnerProperties;

    public PartnerReservationResponse reservation(PartnerReservationRequest request, String transactionId) {
        try {
            return partnerClient.reservation(
                    getUrlByPartnerCode(request.getPartnerCode()),
                    request,
                    getHeaders(Collections.singletonMap(Constants.TRANSACTION_ID, transactionId)));
        } catch (FeignException e) {
            var status = HttpStatus.valueOf(e.status());
            if (status.is5xxServerError()) {
                throw new CartException(CartErrorType.CONNECTOR_INTERNAL_ERROR, "", "", e);
            } else {
                //TODO PENSAR NUMA FORMA DO CONECTOR NOS RESPONDER COM CLAREZA OS ERROS DE NEGÓCIO
                throw new CartException(CartErrorType.CONNECTOR_ERROR, "", e.responseBody().toString(), e);
            }
        } catch (Exception e) {
            throw new CartException(CartErrorType.INTERNAL_ERROR, "", "", e);
        }
    }

    private MultiValueMap<String, String> getHeaders(Map<String, String> mapHeaders) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        mapHeaders.forEach(headers::add);
        return headers;
    }

    private URI getUrlByPartnerCode(String partnerCode) {
        return URI.create(partnerProperties.getUrlByPartnerCode(partnerCode));
    }
}
