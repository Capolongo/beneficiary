package br.com.livelo.orderflight.convert;

import br.com.livelo.orderflight.constants.InstallmentOptionConstant;
import br.com.livelo.orderflight.constants.PaymentOptionConstant;
import br.com.livelo.orderflight.constants.ShipmentOptionConstant;
import br.com.livelo.orderflight.domain.dtos.installment.InstallmentDTO;
import br.com.livelo.orderflight.domain.dtos.installment.InstallmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionDTO;
import br.com.livelo.orderflight.domain.dtos.payment.response.PaymentOptionResponse;
import br.com.livelo.orderflight.domain.dtos.shipment.CommerceItem;
import br.com.livelo.orderflight.domain.dtos.shipment.PackagesDTO;
import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentOptionsResponse;
import br.com.livelo.orderflight.domain.dtos.shipment.ShipmentsDTO;
import br.com.livelo.orderflight.domain.entity.OrderEntity;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionConvert {

    public static InstallmentOptionsResponse buildInstallmentOptionsResponse(OrderEntity order, InstallmentOptionConstant installmentOptionConstant, double amount) {
        return InstallmentOptionsResponse.builder()
                .installmentOptions(buildInstallmentOptions(order, installmentOptionConstant, amount))
                .build();
    }

    public static ShipmentOptionsResponse buildShipmentOptionsResponse(String partnerOrderId, OrderEntity order, ShipmentOptionConstant shipmentOptionConstant) {
        return ShipmentOptionsResponse.builder()
                .shipmentOptions(buildShipmentOptions(partnerOrderId, order, shipmentOptionConstant))
                .build();
    }

    public static PaymentOptionResponse buildPaymentOptionsResponse(PaymentOptionConstant paymentOptionConstant) {
        return PaymentOptionResponse.builder()
                .paymentOptions(buildPaymentOptions(paymentOptionConstant))
                .build();
    }

    private static List<PaymentOptionDTO> buildPaymentOptions(PaymentOptionConstant paymentOptionConstant) {
        log.info("OptionServiceImpl.buildPaymentOptions - start. [paymentOptionConstants]: {}", paymentOptionConstant);
        return Collections.singletonList(PaymentOptionDTO.builder()
                .id(paymentOptionConstant.getId())
                .name(paymentOptionConstant.getName())
                .description(paymentOptionConstant.getDescription())
                .build());
    }

    private static List<ShipmentsDTO> buildShipmentOptions(String partnerOrderId, OrderEntity order, ShipmentOptionConstant shipmentOptionConstant) {
        log.info("OptionConvert.buildShipmentOptions - start. [shipmentConstants]: {}", shipmentOptionConstant);
        return Collections.singletonList(ShipmentsDTO.builder()
                .id(shipmentOptionConstant.getId())
                .currency(shipmentOptionConstant.getCurrency())
                .price(shipmentOptionConstant.getPrice())
                .type(shipmentOptionConstant.getType())
                .description(shipmentOptionConstant.getDescription())
                .packages(buildPackages(partnerOrderId, order))
                .build());
    }

    private static List<PackagesDTO> buildPackages(final String partnerOrderId, final OrderEntity order) {
        return order.getItems().stream()
                .map(item -> PackagesDTO.builder()
                        .deliveryDate(Date.from(order.getLastModifiedDate().toInstant()))
                        .items(Collections.singletonList(item.getSkuId()))
                        .commerceItems(buildCommerceItems(partnerOrderId, item))
                        .build()
                ).collect(Collectors.toList());
    }

    private static List<CommerceItem> buildCommerceItems(final String partnerOrderId, final OrderItemEntity item) {
        return Collections.singletonList(CommerceItem.builder()
                .id(item.getSkuId())
                .partnerOrderId(partnerOrderId)
                .commerceItemId(item.getCommerceItemId())
                .partnerOrderLinkId(item.getPartnerOrderLinkId())
                .build());
    }

    private static List<InstallmentDTO> buildInstallmentOptions(OrderEntity order, InstallmentOptionConstant installmentOptionConstant, double amount) {
        log.info("OptionServiceImpl.buildInstallmentOptions - start [orderId]: {}, [installmentOptionConstants]: {}", order.getId(), installmentOptionConstant);
        return Collections.singletonList(InstallmentDTO.builder()
                .id(installmentOptionConstant.getId())
                .parcels(installmentOptionConstant.getParcels())
                .interest(installmentOptionConstant.getInterest())
                .currency(installmentOptionConstant.getCurrency())
                .amount(amount)
                .build());
    }
}
