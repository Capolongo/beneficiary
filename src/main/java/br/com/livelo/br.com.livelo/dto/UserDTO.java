package br.com.livelo.br.com.livelo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonIgnore
    private Integer id;

    @Size(min = 2, max = 60)
    private String username;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date creationDate;
}
