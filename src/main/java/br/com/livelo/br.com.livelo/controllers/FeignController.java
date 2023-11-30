package br.com.livelo.br.com.livelo.controllers;

import br.com.livelo.br.com.livelo.client.JsonPlaceHolderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/feign")
public class FeignController {

    private final JsonPlaceHolderClient jsonPlaceHolderClient;

    @Autowired
    public FeignController(final JsonPlaceHolderClient jsonPlaceHolderClient) {
        this.jsonPlaceHolderClient = jsonPlaceHolderClient;
    }

    @GetMapping
    public List<Object> getTodos() {
        log.info("Making a request to FeignClient...");
        return jsonPlaceHolderClient.getTodos();
    }

}
