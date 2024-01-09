package br.com.livelo.orderflight.client;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "partner-client")
public interface PartnerClient {

  @GetMapping
  void partnerConnectorUrl(URI baseUrl);
}
