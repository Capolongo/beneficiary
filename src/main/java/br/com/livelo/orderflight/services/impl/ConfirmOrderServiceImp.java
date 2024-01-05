package br.com.livelo.orderflight.services.impl;


import br.com.livelo.orderflight.services.ConfirmOrderService;
import org.springframework.stereotype.Service;

@Service
public class ConfirmOrderServiceImp implements ConfirmOrderService {

    //private PartnersConfigService partnersConfigService;
    public void confirmOrder(){
        //busca pedido pelo id
        validateRequest();
        confirmOnPartner();
        //salva informacoes no OrderService
        //retorna resultado
    }

    private void validateRequest(){

    }

    private void confirmOnPartner(){
        // busca webhook pela classe da lib
        // chama proxy de confirmação
        // retorna resultado por meio do status
    }

}
