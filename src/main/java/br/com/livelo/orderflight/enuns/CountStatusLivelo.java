package br.com.livelo.orderflight.enuns;

import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CountStatusLivelo {
    PROCESSING("LIVPNR-1007", Webhooks.GETCONFIRMATION.value),
    VOUCHER("LIVPNR-1019", Webhooks.VOUCHER.value);

    private final String code;
    private final String description;

    public static CountStatusLivelo getStatus(String status) {
      return  Arrays.stream(CountStatusLivelo.values()).filter(statusLivelo -> statusLivelo.getCode().equals(status)).findFirst().orElse(null);
    }
}
