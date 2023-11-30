package br.com.livelo.br.com.livelo;

import br.com.livelo.log.annotation.EnableLiveloLogAround;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableLiveloLogAround

@EnableFeignClients

@SpringBootApplication
public class LiveloDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiveloDemoApplication.class, args);
    }
}
