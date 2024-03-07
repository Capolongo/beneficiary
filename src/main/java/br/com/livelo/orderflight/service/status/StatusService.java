package br.com.livelo.orderflight.service.status;

import br.com.livelo.orderflight.domain.dtos.confirmation.response.ConfirmOrderResponse;
import br.com.livelo.orderflight.domain.dtos.status.request.UpdateStatusRequest;

public interface StatusService {

    ConfirmOrderResponse updateStatus(String id, UpdateStatusRequest request);
}
