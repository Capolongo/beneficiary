package br.com.livelo.br.com.livelo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String id;

    @Size(min = 2, max = 60)
    private String name;

    @NotNull
    private Double price;

    @Size(min = 2, max = 255)
    private String description;
}
