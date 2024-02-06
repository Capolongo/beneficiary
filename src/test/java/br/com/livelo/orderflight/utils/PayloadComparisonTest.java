package br.com.livelo.orderflight.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.livelo.orderflight.domain.dtos.confirmation.request.ConfirmOrderItemRequest;
import br.com.livelo.orderflight.domain.entity.OrderItemEntity;
import br.com.livelo.orderflight.mock.MockBuilder;

@ExtendWith(MockitoExtension.class)
class PayloadComparisonTest {

  @Test
  void shouldReturnPayloadComparisonAreEqual() {
    Boolean result = PayloadComparison.compareItems(Set.of(MockBuilder.confirmOrderItemRequest()),
        Set.of(MockBuilder.orderItemEntity()));

    assertEquals(true, result);
  }

  @Test
  void shouldReturnPayloadComparisonAreNotEqualBasedOnSize() {
    Boolean result = PayloadComparison.compareItems(Set.of(MockBuilder.confirmOrderItemRequest()),
        Set.of());
    assertEquals(false, result);
  }

  @Test
  void shouldReturnPayloadComparisonAreNotEqualBasedOnCommerceItemOrder() {
    OrderItemEntity flightItemBase = MockBuilder.orderItemEntity();

    ConfirmOrderItemRequest flightItemRequest = MockBuilder.confirmOrderItemRequest();

    flightItemRequest.setCommerceItemId("request_wrong_id");
    flightItemBase.setCommerceItemId("base_wrong_id");


    Boolean result = PayloadComparison.compareItems(Set.of(flightItemRequest),
        Set.of(flightItemBase));
    assertEquals(false, result);
  }

}
