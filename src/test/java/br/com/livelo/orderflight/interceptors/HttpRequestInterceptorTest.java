package br.com.livelo.orderflight.interceptors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class HttpRequestInterceptorTest {
  
private final HttpRequestInterceptor httpRequestInterceptor = new HttpRequestInterceptor();

  @Test
  void shouldInterceptRequest() {
    MDC mockedMDC = mock(MDC.class);
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    Boolean intercepted = httpRequestInterceptor.preHandle(request, response, "doesn't matter");
    
    verifyNoMoreInteractions(mockedMDC);
    assertTrue(intercepted);
  }

  @Test
  void shouldClearInterceptorWhenFinish() {
    MDC mockedMDC = mock(MDC.class);
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    httpRequestInterceptor.postHandle(request, response, "doesn't matter", null);
    
    verifyNoMoreInteractions(mockedMDC);
  }

}
