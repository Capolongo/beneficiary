package br.com.livelo.orderflight.domain.dtos.updateOnPartners;

import lombok.Data;
import java.io.Serializable;

@Data
public class StatusDTO implements Serializable {
    private static final long serialVersionUID = -1446686798267213746L;
    private String code;
    private String message;
    private String details;

}
