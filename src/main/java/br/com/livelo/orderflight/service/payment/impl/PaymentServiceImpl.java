package br.com.livelo.orderflight.service.payment.impl;

import br.com.livelo.orderflight.constants.PaymentOptionConstant;
import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionDTO;
import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionResponse;
import br.com.livelo.orderflight.service.order.OrderService;
import br.com.livelo.orderflight.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOptionConstant paymentOptionConstant;

    private final OrderService orderService;

    @Override
    public PaymentOptionResponse getPaymentOptions(String id, String shipmentOptionId) {
        log.debug("PaymentServiceImpl.getPaymentOptions - start tempPartnerOrderId: [{}], shipmentOptionId: [{}]", id, shipmentOptionId);
        orderService.getOrderById(id);
        PaymentOptionResponse response = buildPaymentOptionsResponse(paymentOptionConstant);

        log.debug("PaymentServiceImpl.getPaymentOptions - end tempPartnerOrderId: [{}], shipmentOptionId: [{}]", id, shipmentOptionId);
        return response;
    }

    private PaymentOptionResponse buildPaymentOptionsResponse(PaymentOptionConstant paymentOptionConstant) {
        return PaymentOptionResponse.builder()
                .paymentOptions(buildPaymentOptions(paymentOptionConstant))
                .build();
    }

    private List<PaymentOptionDTO> buildPaymentOptions(PaymentOptionConstant paymentOptionConstant) {
        log.debug("PaymentServiceImpl.buildPaymentOptions - start. [paymentOptionConstants]: {}", paymentOptionConstant);
        return Collections.singletonList(PaymentOptionDTO.builder()
                .id(paymentOptionConstant.getId())
                .name(paymentOptionConstant.getName())
                .description(paymentOptionConstant.getDescription())
                .build());
    }
}
