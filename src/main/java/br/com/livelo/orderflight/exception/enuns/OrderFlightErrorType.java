package br.com.livelo.orderflight.exception.enuns;

import lombok.Getter;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderFlightErrorType {
    // INTERNAL
    ORDER_FLIGHT_INTERNAL_ERROR("OFCART000", "Erro não esperado",
            "Ocorreu um erro por aqui. Entre em contato com o suporte!", HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR),
    FLIGHT_CONNECTOR_INTERNAL_ERROR("OFCART002", "Erro não esperado",
            "Ocorreu um erro por aqui. Entre em contato com o suporte!", HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR),

    // BUSINESS
    FLIGHT_CONNECTOR_BUSINESS_ERROR("OFCART500", "Parceiro não disponível",
            "Parceiro não está disponível para realização desta ação no momento", HttpStatus.BAD_REQUEST, null),
    ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR("OFCART501", "Erro ao adicionar pedido ao carrinho",
            "Favor tente novamente", HttpStatus.BAD_REQUEST, null),
    ORDER_FLIGHT_DIVERGENT_QUANTITY_ITEMS_BUSINESS_ERROR("OFCART502", "Erro ao adicionar pedido ao carrinho",
            "Favor tente novamente", HttpStatus.BAD_REQUEST, null),

    VALIDATION_ORDER_NOT_FOUND("OFCONFIRMATION000", "Ordem não encontrada", "Ordem não foi encontrada na base de dados", HttpStatus.NOT_FOUND, null),
    VALIDATION_OBJECTS_NOT_EQUAL("OFCONFIRMATION001", "Objetos não são iguais", "Objetos do request body e da base de dados não são iguais", HttpStatus.BAD_REQUEST, null),
    VALIDATION_ALREADY_CONFIRMED("OFCONFIRMATION002", "Ordem já confirmada", "Ordem já foi confirmada anteriormente", HttpStatus.BAD_REQUEST, null);

    private final String code;
    private final String title;
    private final String description;
    private final HttpStatus status;
    private final Level level;

    OrderFlightErrorType(String code, String title, String description, HttpStatus status, Level level) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.status = status;
        this.level = level;
    }
}
