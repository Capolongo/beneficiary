package br.com.livelo.orderflight.exception.enuns;

import lombok.Getter;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

import static br.com.livelo.orderflight.constants.ErrorCategoryConstants.BUSINESS;
import static br.com.livelo.orderflight.constants.ErrorCategoryConstants.INTERNAL;
import static br.com.livelo.orderflight.constants.MessagesConstants.*;

@Getter
public enum OrderFlightErrorType {
    // RESERVATION INTERNAL
    ORDER_FLIGHT_INTERNAL_ERROR(INTERNAL, "OFCART000", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR),
    ORDER_FLIGHT_CONNECTOR_INTERNAL_ERROR(INTERNAL, "OFCART002", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR),
    ORDER_FLIGHT_PRICING_INTERNAL_ERROR(INTERNAL, "OFCART003", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR),
    ORDER_FLIGHT_CONFIG_FLIGHT_INTERNAL_ERROR(INTERNAL, "OFCART004", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR),

    // RESERVATION BUSINESS
    ORDER_FLIGHT_CONNECTOR_BUSINESS_ERROR(BUSINESS, "OFCART400", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, Level.WARN),
    ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR(BUSINESS, "OFCART401", ADD_CART_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, Level.WARN),
    ORDER_FLIGHT_DIVERGENT_QUANTITY_ITEMS_BUSINESS_ERROR(BUSINESS, "OFCART402", ADD_CART_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, Level.WARN),
    ORDER_FLIGHT_PRICING_BUSINESS_ERROR(BUSINESS, "OFCART403", ADD_CART_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, Level.WARN),
    ORDER_FLIGHT_PARTNER_RESERVATION_EXPIRED_BUSINESS_ERROR(BUSINESS, "OFCART404", ADD_CART_TITLE, RESERVATION_EXPIRED_DESCRIPTION, HttpStatus.BAD_REQUEST, Level.WARN),
    ORDER_FLIGHT_CONFIG_FLIGHT_BUSINESS_ERROR(BUSINESS, "OFCART405", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, Level.WARN),
    ORDER_FLIGHT_MAX_ITEMS_BUSINESS_ERROR(BUSINESS, "OFCART406", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, Level.WARN),
    ORDER_FLIGHT_ORDER_STATUS_INVALID_BUSINESS_ERROR(BUSINESS, "OFCART407", DEFAULT_MESSAGE_TITLE, DEFAULT_MESSAGE_DESCRIPTION, HttpStatus.BAD_REQUEST, Level.WARN),

    VALIDATION_ORDER_NOT_FOUND(null, "OFCONFIRMATION000", "Ordem não encontrada", "Ordem não foi encontrada na base de dados", HttpStatus.NOT_FOUND, null),

    VALIDATION_COMMERCE_ITEM_ID_OR_ID_SKU_NOT_FOUND(null, "OFCART404", "CommerceItemId / idSku não encontrada ", "CommerceItemId / SKU não foi encontrada na base de dados", HttpStatus.NOT_FOUND, null),

    VALIDATION_STATUS_COMMERCE_ORDER_ID_NOT_EQUAL_ORDER_ID(null, "OFUPDATESTATUS001", "CommerceOrderId e orderId não são iguais", "CommerceOrderId e orderId não são iguais", HttpStatus.BAD_REQUEST, null),

    VALIDATION_STATUS_ITEMS_COMMERCE_ITEM_ID_NOT_EQUAL_COMMERCE_ITEM_ID(null, "OFUPDATESTATUS002", "CommerceItemId não são iguais com a base", "CommerceItemId não são iguais com a base", HttpStatus.BAD_REQUEST, null),

    VALIDATION_STATUS_INITIAL_CANNOT_PROCESSING(null, "OFUPDATESTATUS003", "Pedido com status INITIAL não pode alterar o status para Processando", "Pedido com status INITIAL não pode alterar o status para Processando", HttpStatus.BAD_REQUEST, null),

    VALIDATION_STATUS_CANCELED_OR_COMPLETED(null, "OFUPDATESTATUS004", "O pedido com status Cancelado / Completo, não pode alterar o pedido", "O pedido com status Cancelado / Completo, não pode alterar o pedido", HttpStatus.BAD_REQUEST, null),

    VALIDATION_STATUS_FROM_ITEM(null, "OFUPDATESTATUS005", "Erro de converção de objeto UpdateStatusRequest para UpdateStatusDTO ", "Erro de converção de objeto UpdateStatusRequest para UpdateStatusDTO", HttpStatus.BAD_REQUEST, null),

    VALIDATION_OBJECTS_NOT_EQUAL(null, "OFCONFIRMATION001", "Objetos não são iguais", "Objetos do request body e da base de dados não são iguais", HttpStatus.BAD_REQUEST, null),
    VALIDATION_ALREADY_CONFIRMED(null, "OFCONFIRMATION002", "Ordem já confirmada", "Ordem já foi confirmada anteriormente", HttpStatus.BAD_REQUEST, null),

    VALIDATION_INVALID_PAGINATION(null, "QUERYPAGINATION000", "Paginação invalida", "Verifique os parâmetros de paginação", HttpStatus.BAD_REQUEST, null);

    private final String category;
    private final String code;
    private final String title;
    private final String description;
    private final HttpStatus status;
    private final Level level;

    OrderFlightErrorType(String category, String code, String title, String description, HttpStatus status, Level level) {
        this.category = category;
        this.code = code;
        this.title = title;
        this.description = description;
        this.status = status;
        this.level = level;
    }
}
