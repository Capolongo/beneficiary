package br.com.livelo.orderflight.config;

import br.com.livelo.orderflight.domain.dtos.partners.PartnersResponseDTO;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Data
@Builder
@Component
@ConfigurationProperties
public class PartnersConfig {

    private HashMap<String, PartnersResponseDTO> partners;

}
