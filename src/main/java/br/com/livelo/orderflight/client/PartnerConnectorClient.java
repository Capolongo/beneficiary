package br.com.livelo.orderflight.client;

import java.net.URI;

import br.com.livelo.orderflight.domain.dtos.ConnectorPartnerConfirmationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "partner-client", url = "http://test")
public interface PartnerConnectorClient {
  @PostMapping
  ResponseEntity<ConnectorPartnerConfirmationDTO> partnerConnectorConfirm(URI baseUrl);
}
