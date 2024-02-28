package br.com.livelo.orderflight.service.option;

import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionResponse;
import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;

public interface OptionService {
    PaymentOptionResponse getPaymentOptions(String id, String shipmentOptionId);

    ShipmentOptionsResponse getShipmentOptions(String id, String postalCode);

    InstallmentOptionsResponse getInstallmentOptions(String id, String paymentOptionId);

}
