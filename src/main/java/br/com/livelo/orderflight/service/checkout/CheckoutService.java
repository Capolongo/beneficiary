package br.com.livelo.orderflight.service.checkout;


import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.service.OrderService;
import br.com.livelo.partnersconfigflightlibrary.dto.WebhookDTO;
import br.com.livelo.partnersconfigflightlibrary.utils.Webhooks;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import br.com.livelo.partnersconfigflightlibrary.services.PartnersConfigService;

@Service
@AllArgsConstructor
public class CheckoutService {
    private PartnersConfigService partnersConfigService;
    private OrderService orderService;
    public void confirmOrder(String partnerCode) throws Exception {
        OrderEntity order = orderService.getOrder("");
        validateRequest();
        confirmOnPartner(partnerCode);
        //salva informacoes no OrderService
        //retorna resultado
    }

    private void validateRequest(){

    }

    private void confirmOnPartner(String partnerCode){

        WebhookDTO webhook = partnersConfigService.getPartnerWebhook(partnerCode, Webhooks.CONFIRMATION);
        // chama proxy de confirmação
        // retorna resultado por meio do status
    }
}
