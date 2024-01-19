package br.com.livelo.orderflight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"br.com.livelo.orderflight", "br.com.livelo.partnersconfigflightlibrary"})
public class OrderFlightApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderFlightApplication.class, args);
    }
}
