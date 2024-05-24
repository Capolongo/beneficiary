package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.dtos.repository.PaginationOrderProcessResponse;
import br.com.livelo.orderflight.domain.entity.OrderCurrentStatusEntity;
import br.com.livelo.orderflight.domain.entity.OrderStatusHistoryEntity;
import javax.annotation.processing.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class OrderProcessMapperImpl implements OrderProcessMapper {

    @Override
    public PaginationOrderProcessResponse pageRepositoryToPaginationResponse(Page<OrderProcess> orderProcess) {
        if ( orderProcess == null ) {
            return null;
        }

        PaginationOrderProcessResponse.PaginationOrderProcessResponseBuilder paginationOrderProcessResponse = PaginationOrderProcessResponse.builder();

        paginationOrderProcessResponse.rows( orderProcessPageablePageSize( orderProcess ) );
        paginationOrderProcessResponse.total( (int) orderProcess.getTotalElements() );
        paginationOrderProcessResponse.totalPages( orderProcess.getTotalPages() );

        paginationOrderProcessResponse.orders( contentToOrderProcess(orderProcess.getContent()) );
        paginationOrderProcessResponse.page( pageCountValidation(orderProcess.getPageable()) );

        return paginationOrderProcessResponse.build();
    }

    @Override
    public OrderStatusHistoryEntity statusHistoryToCurrentStatus(OrderCurrentStatusEntity orderCurrentStatusEntity) {
        if ( orderCurrentStatusEntity == null ) {
            return null;
        }

        OrderStatusHistoryEntity.OrderStatusHistoryEntityBuilder orderStatusHistoryEntity = OrderStatusHistoryEntity.builder();

        orderStatusHistoryEntity.id( orderCurrentStatusEntity.getId() );
        orderStatusHistoryEntity.code( orderCurrentStatusEntity.getCode() );
        orderStatusHistoryEntity.description( orderCurrentStatusEntity.getDescription() );
        orderStatusHistoryEntity.partnerCode( orderCurrentStatusEntity.getPartnerCode() );
        orderStatusHistoryEntity.partnerDescription( orderCurrentStatusEntity.getPartnerDescription() );
        orderStatusHistoryEntity.partnerResponse( orderCurrentStatusEntity.getPartnerResponse() );

        return orderStatusHistoryEntity.build();
    }

    private int orderProcessPageablePageSize(Page<OrderProcess> page) {
        if ( page == null ) {
            return 0;
        }
        Pageable pageable = page.getPageable();
        if ( pageable == null ) {
            return 0;
        }
        int pageSize = pageable.getPageSize();
        return pageSize;
    }
}
