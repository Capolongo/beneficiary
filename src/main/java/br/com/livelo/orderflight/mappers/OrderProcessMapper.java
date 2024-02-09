package br.com.livelo.orderflight.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import br.com.livelo.orderflight.domain.dtos.repository.OrderProcess;
import br.com.livelo.orderflight.domain.dtos.repository.PaginationOrderProcessResponse;

@Mapper(componentModel = "spring")
public interface OrderProcessMapper {
  @Mapping(target = "orders", expression = "java(contentToOrderProcess(orderProcess.getContent()))")
  @Mapping(source = "pageable.pageNumber", target = "page")
  @Mapping(source = "pageable.pageSize", target = "rows")
  @Mapping(source = "totalElements", target = "total")
  PaginationOrderProcessResponse pageRepositoryToPaginationResponse(Page<OrderProcess> orderProcess);

  default List<OrderProcess> contentToOrderProcess(List<OrderProcess> content) {
    if (content.isEmpty()) {
      return List.of();
    }

    return content;
  }
}
