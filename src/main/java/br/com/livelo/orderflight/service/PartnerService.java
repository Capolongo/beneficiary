package br.com.livelo.orderflight.service;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import br.com.livelo.orderflight.client.PartnerClient;
import br.com.livelo.orderflight.domain.dto.PartnerReservationRequest;
import br.com.livelo.orderflight.domain.dto.PartnerReservationResponse;
import br.com.livelo.orderflight.utils.Constants;
import br.com.livelo.orderflight.utils.PartnerProperties;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartnerService {    
    private final PartnerClient partnerClient;
    private final PartnerProperties partnerProperties;

    public PartnerReservationResponse reservation(PartnerReservationRequest request,String transactionId) {
		return partnerClient.reservation(
				getUrlByPartnerCode(request.getPartnerCode()),
				request, 
				getHeaders(Collections.singletonMap(Constants.TRANSACTION_ID, transactionId)));
    }

	private MultiValueMap<String, String> getHeaders(Map<String,String> mapHeaders) {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		mapHeaders.forEach((k, v) -> headers.add(k, v));
		return headers;
	}

	private URI getUrlByPartnerCode(String partnerCode) {
		return URI.create(partnerProperties.getUrlByPartnerCode(partnerCode));
	}
}
