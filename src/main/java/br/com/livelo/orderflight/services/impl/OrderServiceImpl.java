package br.com.livelo.orderflight.services.impl;

import br.com.livelo.orderflight.services.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public void getOrder(String id) {
        //busca pedido pelo id no repositório
        //se nao encontrar, lança uma exception
    }

    @Override
    public void updateConfirmOrder() {

    }
}
