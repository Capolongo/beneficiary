package br.com.livelo.orderflight.client;

import br.com.livelo.orderflight.domain.dtos.updateOnPartners.UpdateOrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(value = "livelo-partners-client", url = "${client.livelopartnersclient.endpoint}")
public interface LiveloPartnersClient {
    @PutMapping("/v2/orders/{orderId}")
    ResponseEntity<Object> updateOrder(@PathVariable String orderId, UpdateOrderRequest updateOrderRequest);
}
