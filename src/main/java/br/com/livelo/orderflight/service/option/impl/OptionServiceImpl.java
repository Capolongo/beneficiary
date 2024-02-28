package br.com.livelo.orderflight.service.option.impl;

import br.com.livelo.orderflight.constants.InstallmentOptionConstant;
import br.com.livelo.orderflight.constants.PaymentOptionConstant;
import br.com.livelo.orderflight.constants.ShipmentOptionConstant;
import br.com.livelo.orderflight.convert.OptionConvert;
import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionResponse;
import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.orderflight.service.option.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final PaymentOptionConstant paymentOptionConstant;

    private final ShipmentOptionConstant shipmentOptionConstant;

    private final InstallmentOptionConstant installmentOptionConstant;

    private final OrderService orderService;

    @Override
    public PaymentOptionResponse getPaymentOptions(String id, String shipmentOptionId) {
        log.info("OptionServiceImpl.getPaymentOptions - start tempPartnerOrderId: [{}], shipmentOptionId: [{}]", id, shipmentOptionId);
        orderService.getOrderById(id);
        PaymentOptionResponse response = OptionConvert.buildPaymentOptionsResponse(paymentOptionConstant);

        log.info("OptionServiceImpl.getPaymentOptions - end tempPartnerOrderId: [{}], shipmentOptionId: [{}]", id, shipmentOptionId);
        return response;
    }
    @Override
    public ShipmentOptionsResponse getShipmentOptions(String id, String postalCode) {
        log.info("OptionServiceImpl.getShipmentOptions - start. tempPartnerOrderId: [{}], postalCode: [{}]", id, postalCode);
        final OrderEntity orderEntity = orderService.getOrderById(id);
        ShipmentOptionsResponse shipmentOptionsResponse = OptionConvert.buildShipmentOptionsResponse(id, orderEntity, shipmentOptionConstant);
        log.info("OptionServiceImpl.getShipmentOptions - end shipmentOptionsResponse: [{}]", shipmentOptionsResponse);
        return shipmentOptionsResponse;
    }

    @Override
    public InstallmentOptionsResponse getInstallmentOptions(String id, String paymentOptionId) {
        log.info("OptionServiceImpl.getInstallmentOptions - start. tempPartnerOrderId: [{}], paymentOptionId: [{}]", id, paymentOptionId);
        final OrderEntity orderEntity = orderService.getOrderById(id);

        double amount = orderEntity.getPrice().getPointsAmount().doubleValue();

        InstallmentOptionsResponse installmentOptionsResponse = OptionConvert.buildInstallmentOptionsResponse(orderEntity, installmentOptionConstant, amount );

        log.info("OptionServiceImpl.getInstallmentOptions - end installmentOptionsResponse: [{}]", installmentOptionsResponse);

        return installmentOptionsResponse;
    }

}
