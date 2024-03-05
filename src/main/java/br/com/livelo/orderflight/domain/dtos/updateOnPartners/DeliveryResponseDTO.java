package br.com.livelo.orderflight.domain.dtos.updateOnPartners;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class DeliveryResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7998066647681367690L;

    private String deliveryTracking;
    private String deliveryDate;
    private List<QueryCommerceItemDeliveryDTO> items;
    private List<TrackingResponseDTO> trackings;
}
