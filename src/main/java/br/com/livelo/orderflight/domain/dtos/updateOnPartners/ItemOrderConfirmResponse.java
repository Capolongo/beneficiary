package br.com.livelo.orderflight.domain.dtos.updateOnPartners;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ItemOrderConfirmResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 862132497045251426L;

    private String id;
    private StatusDTO status;
    private Object partnerInfo;
    private String currency = "BRL";
    private Double price;
    private Integer quantity;
    private String deliveryDate;
    private String commerceItemId;
    private String partnerOrderId;
    private List<DocumentsDTO> documents;
    private Boolean forceUpdate;

}
