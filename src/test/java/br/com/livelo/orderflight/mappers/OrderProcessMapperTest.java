package br.com.livelo.orderflight.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.dtos.repository.PaginationOrderProcessResponse;
import br.com.livelo.orderflight.mock.MockBuilder;

@ExtendWith(MockitoExtension.class)
class OrderProcessMapperTest {

  OrderProcessMapper mapper = Mappers.getMapper(OrderProcessMapper.class);

  @Test
  void shouldMapperPageRepositoryToPaginationResponse() {

    int rows = 4;

    Pageable pagination = PageRequest.of(0, rows);
    Page<OrderProcess> rawPageOrderProcess = new PageImpl<OrderProcess>(MockBuilder.listOfOrderProcess(rows), pagination, 500);
    PaginationOrderProcessResponse mappedValues = mapper.pageRepositoryToPaginationResponse(rawPageOrderProcess);

    assertEquals(MockBuilder.paginationOrderProcessResponse(0, rows), mappedValues);
  }

}
