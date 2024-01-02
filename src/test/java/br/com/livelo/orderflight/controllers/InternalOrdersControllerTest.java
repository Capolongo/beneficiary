package br.com.livelo.orderflight.controllers;

import br.com.livelo.orderflight.entities.OrderEntity;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.utils.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
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

        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(new OrderEntity());

        mockMvc.perform(MockMvcRequestBuilders.post(path).content(createOrderRequest).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}
