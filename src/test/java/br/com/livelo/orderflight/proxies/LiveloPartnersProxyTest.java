package br.com.livelo.orderflight.proxies;

import br.com.livelo.orderflight.client.LiveloPartnersClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

class LiveloPartnersProxyTest {
    @Mock
    private LiveloPartnersClient liveloPartnersClient;

    @InjectMocks
    private LiveloPartnersProxy liveloPartnersProxy;

    @Test
    void shouldUpdateOrderOnLiveloPartners() {

    }
}