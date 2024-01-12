package br.com.livelo.orderflight.client;

import java.net.URI;

import br.com.livelo.orderflight.domain.dtos.connector.response.ConnectorConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.connector.request.ConnectorConfirmOrderRequestDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "partner-client", url = "http://test")
public interface PartnerConnectorClient {
  @PostMapping
  ResponseEntity<ConnectorConfirmOrderResponse> confirmOrder(URI baseUrl,
                                                             @RequestBody ConnectorConfirmOrderRequestDTO ConnectorConfirmOrderRequestDTO);
}
