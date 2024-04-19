
package br.com.livelo.orderflight.interceptors;

import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.livelo.orderflight.constants.DynatraceConstants;
import br.com.livelo.orderflight.constants.HeadersConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HttpRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String transactionId = request.getHeader(HeadersConstants.LIVELO_TRANSACTION_ID_HEADER);
        String userId = request.getHeader(HeadersConstants.LIVELO_USER_ID_HEADER);

        MDC.put(DynatraceConstants.TRANSACTION_ID, transactionId);
        MDC.put(DynatraceConstants.USER_ID, userId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        MDC.clear();
    }
}