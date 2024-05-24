package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusDTO;
import br.com.livelo.orderflight.domain.entity.OrderCurrentStatusEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class StatusMapperImpl implements StatusMapper {

    @Override
    public OrderCurrentStatusEntity convert(UpdateStatusDTO status) {
        if ( status == null ) {
            return null;
        }

        OrderCurrentStatusEntity.OrderCurrentStatusEntityBuilder orderCurrentStatusEntity = OrderCurrentStatusEntity.builder();

        orderCurrentStatusEntity.description( status.getMessage() );
        orderCurrentStatusEntity.code( status.getCode() );
        orderCurrentStatusEntity.partnerCode( status.getCode() );

        return orderCurrentStatusEntity.build();
    }
}
