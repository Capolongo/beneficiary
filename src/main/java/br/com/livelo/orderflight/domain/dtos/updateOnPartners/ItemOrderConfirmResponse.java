package br.com.livelo.orderflight.domain.dtos.updateOnPartners;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ItemOrderConfirmResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 862132497045251426L;

    private String deliveryTracking;
    private String deliveryDate;
    private List<QueryCommerceItemDeliveryDTO> items;
    private List<TrackingResponseDTO> trackings;

}
