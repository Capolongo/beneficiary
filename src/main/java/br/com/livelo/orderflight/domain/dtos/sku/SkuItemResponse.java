package br.com.livelo.orderflight.domain.dtos.sku;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkuItemResponse {
    @JsonProperty("id")
    private String skuId;
    private String description;
    private boolean available;
    private double salePrice;
    private double listPrice;
    private String currency = "PTS";
    private Integer quantity;
    private String commerceItemId;
}
