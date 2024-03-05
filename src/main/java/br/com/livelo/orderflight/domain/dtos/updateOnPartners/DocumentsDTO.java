package br.com.livelo.orderflight.domain.dtos.updateOnPartners;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;

@Data
public class DocumentsDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7932028053294494986L;
    private String code;
    private String type;
    private String descriptor;
    private String url;
}
