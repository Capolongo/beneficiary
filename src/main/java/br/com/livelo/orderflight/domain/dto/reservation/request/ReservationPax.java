package br.com.livelo.orderflight.domain.dto.reservation.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Validated
@ToString
public class ReservationPax {
    private String type;

    @NotBlank
    @Size(min = 1, max = 60)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 60)
    private String lastName;

    private String gender;

    @NotBlank
    private String birthDate;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String areaCode;

    @NotBlank
    private String phoneNumber;

    @Valid
    @NotNull
    private List<ReservationDocument> documents;
}
