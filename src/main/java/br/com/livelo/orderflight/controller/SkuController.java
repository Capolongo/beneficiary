package br.com.livelo.orderflight.controller;

import br.com.livelo.orderflight.domain.dtos.sku.SkuItemResponse;
import br.com.livelo.orderflight.service.sku.SkuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/skus")
public class SkuController {

    private final SkuService skuService;
    @GetMapping("{id}")
    public ResponseEntity<SkuItemResponse> getSku(@PathVariable("id") String id,
                                                  @RequestParam(required= false) final String commerceItemId,
                                                  @RequestParam(required= false) final String currency) {
        log.debug("SkuController.getSku - start id: [{}], commerceItemId: [{}], currency: [{}]", id, commerceItemId, currency);
        SkuItemResponse response = skuService.getSku(id, commerceItemId, currency);
        log.debug("SkuController.getSku - end");
        return ResponseEntity.ok().body(response);
    }
}
