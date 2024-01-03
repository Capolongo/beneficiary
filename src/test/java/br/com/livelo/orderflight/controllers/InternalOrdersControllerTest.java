package br.com.livelo.orderflight.controllers;

import br.com.livelo.orderflight.entities.OrderEntity;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.utils.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(InternalOrdersController.class)
public class InternalOrdersControllerTest {
    private String path = "/v1/internal/orders";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void createOrderSuccess() throws Exception {
        final String createOrderRequest = BaseTest.readFile("json/InternalOrdersController.json");

        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(order(null).get());

        mockMvc.perform(MockMvcRequestBuilders.post(path).content(createOrderRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getOrderSuccess() throws Exception {
        final String id = "lf1";
        final String pathToGetOrder = path + "/" + id;
        final Optional<OrderEntity> expectedResponseFromService = order(id);

        Mockito.when(orderRepository.findById(Mockito.any())).thenReturn(order(id));

        mockMvc.perform(get(pathToGetOrder).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    private Optional<OrderEntity> order(String id) {
        return Optional.of(OrderEntity.builder().commerceOrderId("1").originOrder("1").id(id).partnerOrderId("1").channel("1").customerIdentifier("1")
                .build());
    }
}
