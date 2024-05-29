package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.update.ItemDTO;
import br.com.livelo.orderflight.domain.dtos.update.LegSummaryDTO;
import br.com.livelo.orderflight.domain.dtos.update.StatusDTO;
import br.com.livelo.orderflight.domain.dtos.update.UpdateOrderDTO;
import br.com.livelo.orderflight.domain.entity.*;
import br.com.livelo.orderflight.mock.MockBuilder;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class LiveloPartnersMapperTest {
    LiveloPartnersMapper liveloPartnersMapper = Mappers.getMapper(LiveloPartnersMapper.class);

    @Test
    void shouldMapOrderToUpdateOrderDTO() {
        OrderEntity order = MockBuilder.orderEntity();
        var updateOrderDTO = liveloPartnersMapper.orderEntityToUpdateOrderDTO(order);
        assertInstanceOf(UpdateOrderDTO.class, updateOrderDTO);
    }

    @Test
    void shouldMapOrderItemToItemDTO() {
        OrderItemEntity item = MockBuilder.orderItemEntity();
        var itemDTO = liveloPartnersMapper.orderItemEntityToItemDTO(item);
        assertInstanceOf(ItemDTO.class, itemDTO);
    }



    @Test
    void shouldMapOrderStatusToStatusDTO() {
        OrderCurrentStatusEntity statusEntity = MockBuilder.statusInitial();
        var statusDTO = liveloPartnersMapper.orderStatusEntityToStatusDTO(statusEntity);
        assertInstanceOf(StatusDTO.class, statusDTO);
    }

    @Test
    void shouldMapFlightLegToLegSummaryDTO() {
        FlightLegEntity flightLeg = MockBuilder.flightLegEntity();
        var legSummary = liveloPartnersMapper.flightLegEntityToLegSummaryDTO(flightLeg);
        assertInstanceOf(LegSummaryDTO.class, legSummary);
    }

    @Test
    void shouldReturnSeatClass() {
        FlightLegEntity flightLeg = MockBuilder.flightLegEntity();
        flightLeg.setFareBasis("ECONOMY");
        var legSummary = liveloPartnersMapper.flightLegEntityToLegSummaryDTO(flightLeg);
        assertInstanceOf(LegSummaryDTO.class, legSummary);
    }

    @Test
    void shouldMapOrderToUpdateOrderDTOWithMoreItems() {
        OrderEntity order = MockBuilder.orderEntity();
        var item = MockBuilder.orderItemEntity();
        item.setSkuId("flight");
        item.getTravelInfo().setVoucher("helloworld");
        item.getSegments().add(
                SegmentEntity
                        .builder()
                        .luggages(Set.of(LuggageEntity.builder().type("TO_CHECK_IN").build()))
                        .build()
        );
        order.getItems().add(item);

        var updateOrderDTO = liveloPartnersMapper.orderEntityToUpdateOrderDTO(order);
        assertInstanceOf(UpdateOrderDTO.class, updateOrderDTO);
    }
}
