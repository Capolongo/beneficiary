package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.LiveloPartnersClient;
import br.com.livelo.orderflight.domain.dtos.update.UpdateOrderDTO;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class LiveloPartnersProxy {
    private final LiveloPartnersClient liveloPartnersClient;

    public void updateOrder(String orderId, UpdateOrderDTO updateOrder) {
        try {
            log.info("LiveloPartnersProxy.updateOrder - Start - id: [{}], body: [{}]", orderId, updateOrder);
            ResponseEntity<Object> response = liveloPartnersClient.updateOrder(orderId, updateOrder);
            log.info("LiveloPartnersProxy.updateOrder - End - id: [{}], response: [{}]", orderId, response);
        } catch (FeignException exception) {
            log.error("LiveloPartnersProxy.updateOrder - id: [{}] error [{}]", orderId, exception.getMessage());
            throw exception;
        }
    }
}
