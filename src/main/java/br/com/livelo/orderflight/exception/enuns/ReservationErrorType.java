package br.com.livelo.orderflight.exception.enuns;

import lombok.Getter;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationErrorType {
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

    VALIDATION_ORDER_NOT_FOUND("400", "Order not found",
            "Order not found", HttpStatus.BAD_REQUEST, null),
    VALIDATION_OBJECTS_NOT_EQUAL("400", "Objects are not equal",
            "Objects are not equal", HttpStatus.BAD_REQUEST, null),
    VALIDATION_ALREADY_CONFIRMED("400", "Order is already confirmed",
            "Order is already confirmed", HttpStatus.BAD_REQUEST, null);

    private final String code;
    private final String title;
    private final String description;
    private final HttpStatus status;
    private final Level level;

    ReservationErrorType(String code, String title, String description, HttpStatus status, Level level) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.status = status;
        this.level = level;
    }
}
