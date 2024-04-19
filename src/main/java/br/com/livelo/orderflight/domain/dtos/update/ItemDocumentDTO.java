package br.com.livelo.orderflight.domain.dtos.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDocumentDTO {
    public String name;
    public String code;
    public String type;
    public String descriptor;
    public String url;
}
