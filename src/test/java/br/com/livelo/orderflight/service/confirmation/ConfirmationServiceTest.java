package br.com.livelo.orderflight.service.confirmation;

import br.com.livelo.orderflight.mapper.ConfirmOrderMapper;
import br.com.livelo.orderflight.proxies.ConnectorPartnersProxy;
import br.com.livelo.orderflight.repository.OrderRepository;
import br.com.livelo.orderflight.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConfirmationServiceTest {
    @Mock
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ConfirmOrderMapper confirmOrderMapper;
    @Mock
    private ConnectorPartnersProxy connectorPartnersProxy;

    @InjectMocks
    private ConfirmationService confirmationService;

    @Test
    void shouldValidateIfPartnerOrdersIdsAreEqual() {
    }
}