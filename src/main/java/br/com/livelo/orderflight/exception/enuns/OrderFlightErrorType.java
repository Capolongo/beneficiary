package br.com.livelo.orderflight.exception.enuns;

import lombok.Getter;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

import static br.com.livelo.orderflight.constants.MessagesConstants.*;

@Getter
public enum OrderFlightErrorType {
    // INTERNAL
    ORDER_FLIGHT_INTERNAL_ERROR("OFCART000", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR),
    ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR("OFCART002", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR),
    ORDER_FLIGHT_PRICING_INTERNAL_ERROR("OFCART003", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR),

    // BUSINESS
    ORDER_FLIGHT_CONNECTOR_BUSINESS_ERROR("OFCART400", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, null),
    ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR("OFCART401", ADD_CART_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, null),
    ORDER_FLIGHT_DIVERGENT_QUANTITY_ITEMS_BUSINESS_ERROR("OFCART402", ADD_CART_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, null),
    ORDER_FLIGHT_PRICING_BUSINESS_ERROR("OFCART403", ADD_CART_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, null),

    VALIDATION_ORDER_NOT_FOUND("OFCONFIRMATION000", "Ordem não encontrada", "Ordem não foi encontrada na base de dados", HttpStatus.NOT_FOUND, null),
    VALIDATION_OBJECTS_NOT_EQUAL("OFCONFIRMATION001", "Objetos não são iguais", "Objetos do request body e da base de dados não são iguais", HttpStatus.BAD_REQUEST, null),
    VALIDATION_ALREADY_CONFIRMED("OFCONFIRMATION002", "Ordem já confirmada", "Ordem já foi confirmada anteriormente", HttpStatus.BAD_REQUEST, null),
    
    VALIDATION_INVALID_PAGINATION("QUERYPAGINATION000", "Paginação invalida", "Verifique os parâmetros de paginação", HttpStatus.BAD_REQUEST, null);

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
