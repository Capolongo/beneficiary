package br.com.livelo.orderflight.domain.dtos.headers;

import java.util.HashMap;
import java.util.Map;

import br.com.livelo.orderflight.constants.HeadersConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequiredHeaders {
  private String transactionId;
  private String userId;

  public Map<String, String> getAllRequiredHeaders() {
    Map<String, String> headers = new HashMap<>();
    headers.put(HeadersConstants.LIVELO_TRANSACTION_ID_HEADER, transactionId);
    headers.put(HeadersConstants.LIVELO_USER_ID_HEADER, userId);
    return headers;
  }
}