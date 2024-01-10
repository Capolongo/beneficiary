package br.com.livelo.orderflight.exception.enuns;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CartErrorType {
    INTERNAL_ERROR("OFCART000", "Erro não esperado", "Ocorreu um erro por aqui. Entre em contato com o suporte!", HttpStatus.INTERNAL_SERVER_ERROR),
    CONNECTOR_ERROR("OFCART001", "Parceiro não disponível", "Parceiro não está disponível para realização dessa ação no momento", HttpStatus.BAD_REQUEST),
    CONNECTOR_INTERNAL_ERROR("OFCART002", "Erro não esperado", "Ocorreu um erro por aqui. Entre em contato com o suporte!", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String title;
    private final String description;
    private final HttpStatus status;

    CartErrorType(String code, String title, String description, HttpStatus status) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
