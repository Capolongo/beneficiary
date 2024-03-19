package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.LiveloPartnersClient;
import br.com.livelo.orderflight.domain.dtos.update.UpdateOrderDTO;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LiveloPartnersProxyTest {
    @Mock
    private LiveloPartnersClient liveloPartnersClient;

    @InjectMocks
    private LiveloPartnersProxy liveloPartnersProxy;

    @Test
    void shouldUpdateOrderOnLiveloPartners() {
        UpdateOrderDTO request = UpdateOrderDTO.builder().build();

        when(liveloPartnersClient.updateOrder("id", request)).thenReturn(ResponseEntity.ok().body(""));
        liveloPartnersProxy.updateOrder("id", request);
        verify(liveloPartnersClient, times(1)).updateOrder("id", request);
    }
    @Test
    void shouldThrowException() {
        UpdateOrderDTO request = UpdateOrderDTO.builder().build();
        FeignException mockException = Mockito.mock(FeignException.class);
        when(liveloPartnersClient.updateOrder("id", request)).thenThrow(mockException);
        assertThrows(FeignException.class, () -> liveloPartnersProxy.updateOrder("id", request));
    }
}