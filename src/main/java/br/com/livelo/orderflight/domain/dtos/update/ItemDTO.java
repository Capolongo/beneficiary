package br.com.livelo.orderflight.domain.dtos.update;

import br.com.livelo.orderflight.domain.dtos.updateOnPartners.DocumentsDTO;
import br.com.livelo.orderflight.domain.dtos.updateOnPartners.StatusDTO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ItemDTO implements Serializable {
    private String id;
    private String commerceItemId;
    private String partnerOrderId;
    private StatusDTO status;
    private Long price;
    private Long quantity;
    private String currency;
    private String deliveryDate;
    private List<DocumentDTO> documents;
    private PartnerInfoSummaryDTO partnerInfo;
    private Boolean forceUpdate;

}
