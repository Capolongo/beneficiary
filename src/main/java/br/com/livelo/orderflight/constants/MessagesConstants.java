package br.com.livelo.orderflight.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MessagesConstants {
    public static final String DEFAULT_MESSAGE_TITLE = "Erro não esperado";
    public static final String DEFAULT_MESSAGE_DESCRIPTION = "Ocorreu um erro por aqui. Entre em contato com o suporte!";
    public static final String ADD_CART_TITLE = "Erro ao adicionar pedido ao carrinho";
    public static final String RESERVATION_EXPIRED_DESCRIPTION = "O tempo de reserva para este item expirou! Realize uma nova busca.";

    //detailed messages command center
    public static final String BOOKING_INTERNAL_ERROR = "Erro interno livelo. Responsável #reservando_minha_viagem_alertas";
    public static final String BOOKING_CONNECTOR_INTERNAL_ERROR = "Erro no conector. Responsável #reservando_minha_viagem_alertas";
    public static final String BOOKING_PARTNER_INTERNAL_ERROR = "Erro no parceiro. Responsável %S";
    public static final String BOOKING_PRICING_INTERNAL_ERROR = "Erro interno calculadora. Responsável #time_calculadora_alertas";
    public static final String BOOKING_PARTNER_CONFIG_FLIGHT = "Erro interno partners-config-flight. Responsável #time_xpto_alertas";
}
