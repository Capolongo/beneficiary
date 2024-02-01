package br.com.livelo.orderflight.exception.OrderExceptions;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
