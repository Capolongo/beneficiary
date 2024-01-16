package br.com.livelo.orderflight.exception.enuns;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ReservationErrorType {
    //INTERNAL
    ORDER_FLIGHT_INTERNAL_ERROR("OFCART000", "Erro não esperado", "Ocorreu um erro por aqui. Entre em contato com o suporte!", HttpStatus.INTERNAL_SERVER_ERROR),
    FLIGHT_CONNECTOR_INTERNAL_ERROR("OFCART002", "Erro não esperado", "Ocorreu um erro por aqui. Entre em contato com o suporte!", HttpStatus.INTERNAL_SERVER_ERROR),

    //BUSINESS
    FLIGHT_CONNECTOR_BUSINESS_ERROR("OFCART500", "Parceiro não disponível", "Parceiro não está disponível para realização desta ação no momento", HttpStatus.BAD_REQUEST),
    ORDER_FLIGHT_DIVERGENT_TOKEN_BUSINESS_ERROR("OFCART501", "Erro ao adicionar pedido ao carrinho", "Favor tente novamente", HttpStatus.BAD_REQUEST),
    ORDER_FLIGHT_DIVERGENT_QUANTITY_ITEMS_BUSINESS_ERROR("OFCART502", "Erro ao adicionar pedido ao carrinho", "Favor tente novamente", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String title;
    private final String description;
    private final HttpStatus status;

    ReservationErrorType(String code, String title, String description, HttpStatus status) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
