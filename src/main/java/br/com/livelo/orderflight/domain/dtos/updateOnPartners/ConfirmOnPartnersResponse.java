package br.com.livelo.orderflight.domain.dtos.updateOnPartners;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ConfirmOnPartnersResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -2596689314594482849L;

    private List<ItemOrderConfirmResponse> items = new ArrayList<>();
    private String currency = "BRL";
    private Double amount;
    private List<DeliveryResponseDTO> deliveries;
}
