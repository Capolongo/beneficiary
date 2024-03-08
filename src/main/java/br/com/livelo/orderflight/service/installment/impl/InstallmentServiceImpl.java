package br.com.livelo.orderflight.service.installment.impl;

import br.com.livelo.orderflight.constants.InstallmentOptionConstant;
import br.com.livelo.orderflight.domain.dtos.installment.InstallmentDTO;
import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.service.installment.InstallmentService;
import br.com.livelo.orderflight.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstallmentServiceImpl implements InstallmentService {

    private final InstallmentOptionConstant installmentOptionConstant;

    private final OrderService orderService;

    @Override
    public InstallmentOptionsResponse getInstallmentOptions(String id, String paymentOptionId) {
        log.debug("InstallmentServiceImpl.getInstallmentOptions - start. tempPartnerOrderId: [{}], paymentOptionId: [{}]", id, paymentOptionId);
        final OrderEntity orderEntity = orderService.getOrderById(id);

        BigDecimal amount = orderEntity.getPrice().getPointsAmount();

        InstallmentOptionsResponse installmentOptionsResponse = buildInstallmentOptionsResponse(orderEntity, amount );

        log.debug("InstallmentServiceImpl.getInstallmentOptions - end installmentOptionsResponse: [{}]", installmentOptionsResponse);

        return installmentOptionsResponse;
    }

    private InstallmentOptionsResponse buildInstallmentOptionsResponse(OrderEntity order, BigDecimal amount) {
        return InstallmentOptionsResponse.builder()
                .installmentOptions(buildInstallmentOptions(order, amount))
                .build();
    }

    private List<InstallmentDTO> buildInstallmentOptions(OrderEntity order, BigDecimal amount) {

        return Collections.singletonList(InstallmentDTO.builder()
                .id(installmentOptionConstant.getId())
                .parcels(installmentOptionConstant.getParcels())
                .interest(installmentOptionConstant.getInterest())
                .currency(installmentOptionConstant.getCurrency())
                .amount(amount)
                .build());
    }

}
