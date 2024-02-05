# Order Flight - Confirmation Order

API responsável por solicitar a confirmação do pedido por meio do connector do parceiro.

## Endpoint

/v1/orders/{id}/confirmation

## Contexto

Para que ocorra a confirmação da ordem, o Order Flight irá solicitar a confirmação ao parceiro por meio do connector, utilizando a URL cadastrada no webhook de confirmação. Além disso, o serviço é encarregado de gerenciar todo o tratamento de erros, desde a solicitação inicial até a atualização da ordem no banco de dados Oracle. Ele desempenha um papel integral na comunicação eficaz com o parceiro, aproveitando a URL registrada no webhook de confirmação, ao mesmo tempo em que assume a responsabilidade pela manipulação exceção que possa ocorrer durante esse processo, garantindo uma integração fluida e confiável. Posteriormente, o serviço atualiza os detalhes da ordem no banco de dados, mantendo a consistência e a precisão das informações registradas.

## Regras:

- A Ordem deve estar salva no banco de dados, caso o contrário será lançado o erro `Status: 404 -> Order not found`;
- O Parceiro deve estar ativo para que confirmação seja bem sucedida, caso contrário o pedido irá cair em falha (LIVPNR-1014 - FAILED);
- O body da requisição não pode ter divergências dos valores da ordem que está salva na base (commerceOrderId, pointsAmount, items.length), caso haja divergência será lançado o erro `Status: 400 -> Objects are not equal`;
- O current status da ordem deve ser `LIVPNR-1006` ou `resubmission=true` caso o contrário será lançado o erro `Status: 400 -> Order is already confirmed`;

### Diagrama:

![Diagrama de fluxo.](images/diagrama.png)

## Mocks

Para os cenários de confirmação, temos alguns mocks configurados no projeto citado na aba "Mockando dependências". A seguir, iremos exemplificar quais cenários existem e como faz para cair em cada cenário.

### Solicitação de confirmação no parceiro -> Status: em processamento

**Descrição do cenário:** Esse é o cenário onde o parceiro retorna que o pedido está sendo processado e ainda não tem um resultado final sobre a confirmação. O status a ser retornado é o **LIVPNR-1007 - PROCESSING**

**Request:**
POST /order-flight/v1/orders/**lf206**/confirmation
Payload: qualquer payload com a estrutura abaixo:

<details>
  <summary>Clique para ver o payload</summary>
    <pre>
        {
            "id": "lf206",
            "resubmission": false,
            "commerceOrderId": "o1002",
            "commeceItemId": "ci001",
            "partnerCode": "cvc",
            "customerId": "0000",
            "customerProfileId": "122518923",
            "partnerOrderId": "8484848",
            "submittedDate": "2024-01-18T23:52:47Z",
            "channel": "I",
            "originOfOrder": "default",
            "price": {
                "pointsAmount": 20000,
                "priceListId": "priceListClub"
            },
            "items": [
                {
                    "commerceItemId": "ci16238303923791",
                    "skuId": "cvc_flight",
                    "productType": "type_flight",
                    "productId": "flight",
                    "externalCoupon": "cupon10",
                    "quantity": 1,
                    "price": {
                        "pointsAmount": 15000,
                        "priceListId": "priceListClub"
                    }
                },
                {
                    "commerceItemId": "ci13406264327442",
                    "skuId": "cvc_flight_tax",
                    "productId": "flight",
                    "productType": "type_flight_tax",
                    "externalCoupon": "cupon10",
                    "quantity": 1,
                    "price": {
                        "pointsAmount": 5000,
                        "priceListId": "priceListClub"
                    }
                }
            ]
        }
    </pre>
</details>

<details>
  <summary>Clique para ver o Response</summary>
    <pre>
{
    "id": "lf206",
    "commerceOrderId": "o1000",
    "partnerOrderId": "5010",
    "partnerCode": "cvc",
    "submittedDate": "2023-09-21T14:49:01",
    "expirationDate": "2023-09-21T14:49:01",
    "transactionId": "uuid",
    "status": {
        "code": "LIVPRN-1007",
        "description": "PROCESSING",
        "details": "Pedido confirmado em processamento"
    },
    "price": {
        "amount": 1999.99,
        "pointsAmount": 150000,
        "priceListId": "priceListClub"
    },
    "items": [
        {
            "commeceItemId": "ci001",
            "skuId": "cvc_flight",
            "productId": "flight",
            "quantity": 1,
            "externalCoupon": "cupon10",
            "productType": "type_flight",
            "price": {
                "amount": 1500.0,
                "pointsAmount": 130000,
                "priceListId": "priceListClub"
            },
            "travelInfo": {
                "type": "ROUND_TRIP",
                "reservationCode": "YD5WK",
                "adultQuantity": 2,
                "childQuantity": 0,
                "babyQuantity": 0,
                "typeClass": "ECONOMIC",
                "paxs": [
                    {
                        "type": "ADULT",
                        "firstName": "João",
                        "lastName": "Silva",
                        "gender": "M",
                        "birthDate": "1995-10-10",
                        "document": "12345678901",
                        "documentType": "CPF",
                        "email": "email@gmail.com",
                        "areaCode": "54",
                        "phone": "999999999"
                    },
                    {
                        "type": "ADULT",
                        "firstName": "Maria",
                        "lastName": "Silva",
                        "gender": "F",
                        "birthDate": "1997-09-16",
                        "document": "12345678901",
                        "documentType": "CPF",
                        "email": "email@gmail.com",
                        "areaCode": "54",
                        "phone": "999999999"
                    }
                ],
                "voucher": "link.voucher.com.br"
            },
            "segments": [
                {
                    "partnerId": "a.sadsa84s4ds",
                    "step": "0",
                    "stops": 0,
                    "flightDuration": 30,
                    "originIata": "POA",
                    "originDescription": "Guarulhos - SP - Brasil",
                    "destinationIata": "GIG",
                    "destinationDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                    "departureDate": "2024-09-21T15:45:00",
                    "arrivalDate": "2024-09-21T16:15:00",
                    "flightsLegs": [
                        {
                            "flightNumber": "1045",
                            "flightDuration": 25,
                            "airline": "GOL",
                            "managedBy": "GOL",
                            "operatedBy": "GOL",
                            "timeToWait": null,
                            "originIata": "GUA",
                            "originDescription": "Guarulhos - SP - Brasil",
                            "destinationIata": "GIG",
                            "destinationDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                            "departureDate": "2024-09-21T15:45:00",
                            "arrivalDate": "2024-09-21T16:15:00",
                            "type": "FINAL"
                        }
                    ],
                    "luggages": [
                        {
                            "type": "HAND",
                            "description": "Bagagem de mão"
                        },
                        {
                            "type": "BAG",
                            "description": "Bagagem despachada"
                        }
                    ],
                    "cancelationRules": [
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
                        },
                        {
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        }
                    ],
                    "changeRules": [
                        {
                            "type": "1",
                            "description": "Não permite cancelamento"
                        }
                    ]
                },
                {
                    "partnerId": "a.sadsa84s",
                    "step": "0",
                    "stops": 0,
                    "flightDuration": 30,
                    "originIata": "GIG",
                    "originDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                    "destinationIata": "GUA",
                    "destinationDescription": "Guarulhos - SP - Brasil",
                    "departureDate": "2024-09-21T15:45:00",
                    "arrivalDate": "2024-09-21T16:15:00",
                    "flightsLegs": [
                        {
                            "flightNumber": "1045",
                            "flightDuration": 25,
                            "airline": "GOL",
                            "managedBy": "GOL",
                            "operatedBy": "GOL",
                            "timeToWait": null,
                            "originIata": "GIG",
                            "originDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                            "destinationIata": "GUA",
                            "destinationDescription": "Guarulhos - SP - Brasil",
                            "departureDate": "2024-09-21T15:45:00",
                            "arrivalDate": "2024-09-21T16:15:00",
                            "type": "FINAL"
                        }
                    ],
                    "luggages": [
                        {
                            "type": "HAND",
                            "description": "Bagagem de mão"
                        },
                        {
                            "type": "BAG",
                            "description": "Bagagem despachada"
                        }
                    ],
                    "cancelationRules": [
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
                        },
                        {
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        }
                    ],
                    "changeRules": [
                        {
                            "type": "1",
                            "description": "Não permite cancelamento"
                        }
                    ]
                }
            ]
        },
        {
            "commeceItemId": "ci002",
            "skuId": "cvc_flight_tax",
            "productId": "flight",
            "quantity": 1,
            "productType": "type_flight_tax",
            "externalCoupon": "cupon10",
            "price": {
                "amount": 499.99,
                "pointsAmount": 20000,
                "priceListId": "priceListClub"
            }
        }
    ]
}
</pre>
</details>

### Pedido confirmado no parceiro

**Descrição do cenário:** Esse é o cenário onde o parceiro retorna que o pedido está confirmado. O status a ser retornado é o **LIVPNR-1019 - PROCESSING**

**Request:**
POST /order-flight/v1/orders/**lf200**/confirmation
Payload: qualquer payload com a estrutura abaixo:

<details>
  <summary>Clique para ver o payload</summary>
    <pre>
{
    "id": "lf200",
    "resubmission": false,
    "commerceOrderId": "o1002",
    "commeceItemId": "ci001",
    "partnerCode": "cvc",
    "customerId": "0000",
    "customerProfileId": "122518923",
    "partnerOrderId": "8484848",
    "submittedDate": "2024-01-18T23:52:47Z",
    "channel": "I",
    "originOfOrder": "default",
    "price": {
        "pointsAmount": 20000,
        "priceListId": "priceListClub"
    },
    "items": [
        {
            "commerceItemId": "ci16238303923791",
            "skuId": "cvc_flight",
            "productType": "type_flight",
            "productId": "flight",
            "externalCoupon": "cupon10",
            "quantity": 1,
            "price": {
                "pointsAmount": 15000,
                "priceListId": "priceListClub"
            }
        },
        {
            "commerceItemId": "ci13406264327442",
            "skuId": "cvc_flight_tax",
            "productId": "flight",
            "productType": "type_flight_tax",
            "externalCoupon": "cupon10",
            "quantity": 1,
            "price": {
                "pointsAmount": 5000,
                "priceListId": "priceListClub"
            }
        }
    ]
}
    </pre>
</details>

<details>
    <summary>Clique para ver o Response</summary>
    <pre>
{
    "id": "lf200",
    "commerceOrderId": "o10000",
    "partnerOrderId": "5010",
    "partnerCode": "cvc",
    "submittedDate": "2023-09-21T14:49:01",
    "expirationDate": "2023-09-21T14:49:01",
    "transactionId": "uuid",
    "status": {
        "code": "LIVPRN-1019",
        "description": "PROCESSING",
        "details": "Pedido confirmado com sucesso"
    },
    "price": {
        "amount": 1999.99,
        "pointsAmount": 150000,
        "priceListId": "priceListClub"
    },
    "items": [
        {
            "commeceItemId": "ci001",
            "skuId": "cvc_flight",
            "productId": "flight",
            "quantity": 1,
            "externalCoupon": "cupon10",
            "productType": "type_flight",
            "price": {
                "amount": 1500.0,
                "pointsAmount": 130000,
                "priceListId": "priceListClub"
            },
            "travelInfo": {
                "type": "ROUND_TRIP",
                "reservationCode": "YD5WK",
                "adultQuantity": 2,
                "childQuantity": 0,
                "babyQuantity": 0,
                "typeClass": "ECONOMIC",
                "paxs": [
                    {
                        "type": "ADULT",
                        "firstName": "João",
                        "lastName": "Silva",
                        "gender": "M",
                        "birthDate": "1995-10-10",
                        "document": "12345678901",
                        "documentType": "CPF",
                        "email": "email@gmail.com",
                        "areaCode": "54",
                        "phone": "999999999"
                    },
                    {
                        "type": "ADULT",
                        "firstName": "Maria",
                        "lastName": "Silva",
                        "gender": "F",
                        "birthDate": "1997-09-16",
                        "document": "12345678901",
                        "documentType": "CPF",
                        "email": "email@gmail.com",
                        "areaCode": "54",
                        "phone": "999999999"
                    }
                ],
                "voucher": "link.voucher.com.br"
            },
            "segments": [
                {
                    "partnerId": "a.sadsa84s4ds",
                    "step": "0",
                    "stops": 0,
                    "flightDuration": 30,
                    "originIata": "POA",
                    "originDescription": "Guarulhos - SP - Brasil",
                    "destinationIata": "GIG",
                    "destinationDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                    "departureDate": "2024-09-21T15:45:00",
                    "arrivalDate": "2024-09-21T16:15:00",
                    "flightsLegs": [
                        {
                            "flightNumber": "1045",
                            "flightDuration": 25,
                            "airline": "GOL",
                            "managedBy": "GOL",
                            "operatedBy": "GOL",
                            "timeToWait": null,
                            "originIata": "GUA",
                            "originDescription": "Guarulhos - SP - Brasil",
                            "destinationIata": "GIG",
                            "destinationDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                            "departureDate": "2024-09-21T15:45:00",
                            "arrivalDate": "2024-09-21T16:15:00",
                            "type": "FINAL"
                        }
                    ],
                    "luggages": [
                        {
                            "type": "HAND",
                            "description": "Bagagem de mão"
                        },
                        {
                            "type": "BAG",
                            "description": "Bagagem despachada"
                        }
                    ],
                    "cancelationRules": [
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
                        },
                        {
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        }
                    ],
                    "changeRules": [
                        {
                            "type": "1",
                            "description": "Não permite cancelamento"
                        }
                    ]
                },
                {
                    "partnerId": "a.sadsa84s",
                    "step": "0",
                    "stops": 0,
                    "flightDuration": 30,
                    "originIata": "GIG",
                    "originDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                    "destinationIata": "GUA",
                    "destinationDescription": "Guarulhos - SP - Brasil",
                    "departureDate": "2024-09-21T15:45:00",
                    "arrivalDate": "2024-09-21T16:15:00",
                    "flightsLegs": [
                        {
                            "flightNumber": "1045",
                            "flightDuration": 25,
                            "airline": "GOL",
                            "managedBy": "GOL",
                            "operatedBy": "GOL",
                            "timeToWait": null,
                            "originIata": "GIG",
                            "originDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                            "destinationIata": "GUA",
                            "destinationDescription": "Guarulhos - SP - Brasil",
                            "departureDate": "2024-09-21T15:45:00",
                            "arrivalDate": "2024-09-21T16:15:00",
                            "type": "FINAL"
                        }
                    ],
                    "luggages": [
                        {
                            "type": "HAND",
                            "description": "Bagagem de mão"
                        },
                        {
                            "type": "BAG",
                            "description": "Bagagem despachada"
                        }
                    ],
                    "cancelationRules": [
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
                        },
                        {
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        }
                    ],
                    "changeRules": [
                        {
                            "type": "1",
                            "description": "Não permite cancelamento"
                        }
                    ]
                }
            ]
        },
        {
            "commeceItemId": "ci002",
            "skuId": "cvc_flight_tax",
            "productId": "flight",
            "quantity": 1,
            "productType": "type_flight_tax",
            "externalCoupon": "cupon10",
            "price": {
                "amount": 499.99,
                "pointsAmount": 20000,
                "priceListId": "priceListClub"
            }
        }
    ]
}
    </pre>
<details>

### Pedido com falha

**Descrição do cenário:** Esse é o cenário onde o parceiro retorna que o ocorreu alguma falha no pedido e que necessita de uma ação manual. O status a ser retornado é o **LIVPNR-1014 - FAILED**

**Request:**
POST /order-flight/v1/orders/**lf400**/confirmation
Payload: qualquer payload com a estrutura abaixo:

<details>
  <summary>Clique para ver o payload</summary>
    <pre>
{
    "id": "lf400",
    "resubmission": false,
    "commerceOrderId": "o1002",
    "commeceItemId": "ci001",
    "partnerCode": "cvc",
    "customerId": "0000",
    "customerProfileId": "122518923",
    "partnerOrderId": "8484848",
    "submittedDate": "2024-01-18T23:52:47Z",
    "channel": "I",
    "originOfOrder": "default",
    "price": {
        "pointsAmount": 20000,
        "priceListId": "priceListClub"
    },
    "items": [
        {
            "commerceItemId": "ci16238303923791",
            "skuId": "cvc_flight",
            "productType": "type_flight",
            "productId": "flight",
            "externalCoupon": "cupon10",
            "quantity": 1,
            "price": {
                "pointsAmount": 15000,
                "priceListId": "priceListClub"
            }
        },
        {
            "commerceItemId": "ci13406264327442",
            "skuId": "cvc_flight_tax",
            "productId": "flight",
            "productType": "type_flight_tax",
            "externalCoupon": "cupon10",
            "quantity": 1,
            "price": {
                "pointsAmount": 5000,
                "priceListId": "priceListClub"
            }
        }
    ]
}
    </pre>
</details>

<details>
    <summary>Clique para ver o Response</summary>
    <pre>
{
    "id": "lf400",
    "commerceOrderId": "o1000",
    "partnerOrderId": "5010",
    "partnerCode": "cvc",
    "submittedDate": "2023-09-21T14:49:01",
    "expirationDate": "2023-09-21T14:49:01",
    "transactionId": "uuid",
    "status": {
        "code": "LIVPRN-1014",
        "description": "FAILED",
        "details": "Os dados fornecidos na solicitação são inválidos ou incompletos."
    },
    "price": {
        "amount": 1999.99,
        "pointsAmount": 150000,
        "priceListId": "priceListClub"
    },
    "items": [
        {
            "commeceItemId": "ci001",
            "skuId": "cvc_flight",
            "productId": "flight",
            "quantity": 1,
            "externalCoupon": "cupon10",
            "productType": "type_flight",
            "price": {
                "amount": 1500.0,
                "pointsAmount": 130000,
                "priceListId": "priceListClub"
            },
            "travelInfo": {
                "type": "ROUND_TRIP",
                "reservationCode": "YD5WK",
                "adultQuantity": 2,
                "childQuantity": 0,
                "babyQuantity": 0,
                "typeClass": "ECONOMIC",
                "paxs": [
                    {
                        "type": "ADULT",
                        "firstName": "João",
                        "lastName": "Silva",
                        "gender": "M",
                        "birthDate": "1995-10-10",
                        "document": "12345678901",
                        "documentType": "CPF",
                        "email": "email@gmail.com",
                        "areaCode": "54",
                        "phone": "999999999"
                    },
                    {
                        "type": "ADULT",
                        "firstName": "Maria",
                        "lastName": "Silva",
                        "gender": "F",
                        "birthDate": "1997-09-16",
                        "document": "12345678901",
                        "documentType": "CPF",
                        "email": "email@gmail.com",
                        "areaCode": "54",
                        "phone": "999999999"
                    }
                ],
                "voucher": "link.voucher.com.br"
            },
            "segments": [
                {
                    "partnerId": "a.sadsa84s4ds",
                    "step": "0",
                    "stops": 0,
                    "flightDuration": 30,
                    "originIata": "POA",
                    "originDescription": "Guarulhos - SP - Brasil",
                    "destinationIata": "GIG",
                    "destinationDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                    "departureDate": "2024-09-21T15:45:00",
                    "arrivalDate": "2024-09-21T16:15:00",
                    "flightsLegs": [
                        {
                            "flightNumber": "1045",
                            "flightDuration": 25,
                            "airline": "GOL",
                            "managedBy": "GOL",
                            "operatedBy": "GOL",
                            "timeToWait": null,
                            "originIata": "GUA",
                            "originDescription": "Guarulhos - SP - Brasil",
                            "destinationIata": "GIG",
                            "destinationDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                            "departureDate": "2024-09-21T15:45:00",
                            "arrivalDate": "2024-09-21T16:15:00",
                            "type": "FINAL"
                        }
                    ],
                    "luggages": [
                        {
                            "type": "HAND",
                            "description": "Bagagem de mão"
                        },
                        {
                            "type": "BAG",
                            "description": "Bagagem despachada"
                        }
                    ],
                    "cancelationRules": [
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
                        },
                        {
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        }
                    ],
                    "changeRules": [
                        {
                            "type": "1",
                            "description": "Não permite cancelamento"
                        }
                    ]
                },
                {
                    "partnerId": "a.sadsa84s",
                    "step": "0",
                    "stops": 0,
                    "flightDuration": 30,
                    "originIata": "GIG",
                    "originDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                    "destinationIata": "GUA",
                    "destinationDescription": "Guarulhos - SP - Brasil",
                    "departureDate": "2024-09-21T15:45:00",
                    "arrivalDate": "2024-09-21T16:15:00",
                    "flightsLegs": [
                        {
                            "flightNumber": "1045",
                            "flightDuration": 25,
                            "airline": "GOL",
                            "managedBy": "GOL",
                            "operatedBy": "GOL",
                            "timeToWait": null,
                            "originIata": "GIG",
                            "originDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                            "destinationIata": "GUA",
                            "destinationDescription": "Guarulhos - SP - Brasil",
                            "departureDate": "2024-09-21T15:45:00",
                            "arrivalDate": "2024-09-21T16:15:00",
                            "type": "FINAL"
                        }
                    ],
                    "luggages": [
                        {
                            "type": "HAND",
                            "description": "Bagagem de mão"
                        },
                        {
                            "type": "BAG",
                            "description": "Bagagem despachada"
                        }
                    ],
                    "cancelationRules": [
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
                        },
                        {
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        }
                    ],
                    "changeRules": [
                        {
                            "type": "1",
                            "description": "Não permite cancelamento"
                        }
                    ]
                }
            ]
        },
        {
            "commeceItemId": "ci002",
            "skuId": "cvc_flight_tax",
            "productId": "flight",
            "quantity": 1,
            "productType": "type_flight_tax",
            "externalCoupon": "cupon10",
            "price": {
                "amount": 499.99,
                "pointsAmount": 20000,
                "priceListId": "priceListClub"
            }
        }
    ]
}
    </pre>
<details>

### Pedido cancelado

**Descrição do cenário:** Esse é o cenário onde o parceiro retorna que o pedido não pode ser processado e ele deve ser cancelado automaticamente. O status a ser retornado é o **LIVPNR-9001 - CANCELLED**

**Request:**
POST /order-flight/v1/orders/**lf400c**/confirmation
Payload: qualquer payload com a estrutura abaixo:

<details>
  <summary>Clique para ver o payload</summary>
    <pre>
{
    "id": "lf400c",
    "resubmission": false,
    "commerceOrderId": "o1002",
    "commeceItemId": "ci001",
    "partnerCode": "cvc",
    "customerId": "0000",
    "customerProfileId": "122518923",
    "partnerOrderId": "8484848",
    "submittedDate": "2024-01-18T23:52:47Z",
    "channel": "I",
    "originOfOrder": "default",
    "price": {
        "pointsAmount": 20000,
        "priceListId": "priceListClub"
    },
    "items": [
        {
            "commerceItemId": "ci16238303923791",
            "skuId": "cvc_flight",
            "productType": "type_flight",
            "productId": "flight",
            "externalCoupon": "cupon10",
            "quantity": 1,
            "price": {
                "pointsAmount": 15000,
                "priceListId": "priceListClub"
            }
        },
        {
            "commerceItemId": "ci13406264327442",
            "skuId": "cvc_flight_tax",
            "productId": "flight",
            "productType": "type_flight_tax",
            "externalCoupon": "cupon10",
            "quantity": 1,
            "price": {
                "pointsAmount": 5000,
                "priceListId": "priceListClub"
            }
        }
    ]
}
    </pre>
</details>

<details>
    <summary>Clique para ver o Response</summary>
    <pre>
{
    "id": "lf400c",
    "commerceOrderId": "o1000",
    "partnerOrderId": "5010",
    "partnerCode": "cvc",
    "submittedDate": "2023-09-21T14:49:01",
    "expirationDate": "2023-09-21T14:49:01",
    "transactionId": "uuid",
    "status": {
        "code": "LIVPRN-9001",
        "description": "CANCELED",
        "details": "Os dados fornecidos na solicitação são inválidos ou incompletos."
    },
    "price": {
        "amount": 1999.99,
        "pointsAmount": 150000,
        "priceListId": "priceListClub"
    },
    "items": [
        {
            "commeceItemId": "ci001",
            "skuId": "cvc_flight",
            "productId": "flight",
            "quantity": 1,
            "externalCoupon": "cupon10",
            "productType": "type_flight",
            "price": {
                "amount": 1500.0,
                "pointsAmount": 130000,
                "priceListId": "priceListClub"
            },
            "travelInfo": {
                "type": "ROUND_TRIP",
                "reservationCode": "YD5WK",
                "adultQuantity": 2,
                "childQuantity": 0,
                "babyQuantity": 0,
                "typeClass": "ECONOMIC",
                "paxs": [
                    {
                        "type": "ADULT",
                        "firstName": "João",
                        "lastName": "Silva",
                        "gender": "M",
                        "birthDate": "1995-10-10",
                        "document": "12345678901",
                        "documentType": "CPF",
                        "email": "email@gmail.com",
                        "areaCode": "54",
                        "phone": "999999999"
                    },
                    {
                        "type": "ADULT",
                        "firstName": "Maria",
                        "lastName": "Silva",
                        "gender": "F",
                        "birthDate": "1997-09-16",
                        "document": "12345678901",
                        "documentType": "CPF",
                        "email": "email@gmail.com",
                        "areaCode": "54",
                        "phone": "999999999"
                    }
                ],
                "voucher": "link.voucher.com.br"
            },
            "segments": [
                {
                    "partnerId": "a.sadsa84s4ds",
                    "step": "0",
                    "stops": 0,
                    "flightDuration": 30,
                    "originIata": "POA",
                    "originDescription": "Guarulhos - SP - Brasil",
                    "destinationIata": "GIG",
                    "destinationDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                    "departureDate": "2024-09-21T15:45:00",
                    "arrivalDate": "2024-09-21T16:15:00",
                    "flightsLegs": [
                        {
                            "flightNumber": "1045",
                            "flightDuration": 25,
                            "airline": "GOL",
                            "managedBy": "GOL",
                            "operatedBy": "GOL",
                            "timeToWait": null,
                            "originIata": "GUA",
                            "originDescription": "Guarulhos - SP - Brasil",
                            "destinationIata": "GIG",
                            "destinationDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                            "departureDate": "2024-09-21T15:45:00",
                            "arrivalDate": "2024-09-21T16:15:00",
                            "type": "FINAL"
                        }
                    ],
                    "luggages": [
                        {
                            "type": "HAND",
                            "description": "Bagagem de mão"
                        },
                        {
                            "type": "BAG",
                            "description": "Bagagem despachada"
                        }
                    ],
                    "cancelationRules": [
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
                        },
                        {
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        }
                    ],
                    "changeRules": [
                        {
                            "type": "1",
                            "description": "Não permite cancelamento"
                        }
                    ]
                },
                {
                    "partnerId": "a.sadsa84s",
                    "step": "0",
                    "stops": 0,
                    "flightDuration": 30,
                    "originIata": "GIG",
                    "originDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                    "destinationIata": "GUA",
                    "destinationDescription": "Guarulhos - SP - Brasil",
                    "departureDate": "2024-09-21T15:45:00",
                    "arrivalDate": "2024-09-21T16:15:00",
                    "flightsLegs": [
                        {
                            "flightNumber": "1045",
                            "flightDuration": 25,
                            "airline": "GOL",
                            "managedBy": "GOL",
                            "operatedBy": "GOL",
                            "timeToWait": null,
                            "originIata": "GIG",
                            "originDescription": "Rio de Janeiro, Galeao - RJ - Brasil",
                            "destinationIata": "GUA",
                            "destinationDescription": "Guarulhos - SP - Brasil",
                            "departureDate": "2024-09-21T15:45:00",
                            "arrivalDate": "2024-09-21T16:15:00",
                            "type": "FINAL"
                        }
                    ],
                    "luggages": [
                        {
                            "type": "HAND",
                            "description": "Bagagem de mão"
                        },
                        {
                            "type": "BAG",
                            "description": "Bagagem despachada"
                        }
                    ],
                    "cancelationRules": [
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
                        },
                        {
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        }
                    ],
                    "changeRules": [
                        {
                            "type": "1",
                            "description": "Não permite cancelamento"
                        }
                    ]
                }
            ]
        },
        {
            "commeceItemId": "ci002",
            "skuId": "cvc_flight_tax",
            "productId": "flight",
            "quantity": 1,
            "productType": "type_flight_tax",
            "externalCoupon": "cupon10",
            "price": {
                "amount": 499.99,
                "pointsAmount": 20000,
                "priceListId": "priceListClub"
            }
        }
    ]
}
    </pre>
<details>

### Erro desconhecido

**Descrição do cenário:** Esse é o cenário onde o order-flight ocasionou algum erro que não pode ser tratado. O status da request será um erro 500.

**Request:**
POST /order-flight/v1/orders/**lf500**/confirmation
Payload: qualquer payload com a estrutura abaixo:

<details>
  <summary>Clique para ver o payload</summary>
    <pre>
{
    "id": "lf500",
    "resubmission": false,
    "commerceOrderId": "o1002",
    "commeceItemId": "ci001",
    "partnerCode": "cvc",
    "customerId": "0000",
    "customerProfileId": "122518923",
    "partnerOrderId": "8484848",
    "submittedDate": "2024-01-18T23:52:47Z",
    "channel": "I",
    "originOfOrder": "default",
    "price": {
        "pointsAmount": 20000,
        "priceListId": "priceListClub"
    },
    "items": [
        {
            "commerceItemId": "ci16238303923791",
            "skuId": "cvc_flight",
            "productType": "type_flight",
            "productId": "flight",
            "externalCoupon": "cupon10",
            "quantity": 1,
            "price": {
                "pointsAmount": 15000,
                "priceListId": "priceListClub"
            }
        },
        {
            "commerceItemId": "ci13406264327442",
            "skuId": "cvc_flight_tax",
            "productId": "flight",
            "productType": "type_flight_tax",
            "externalCoupon": "cupon10",
            "quantity": 1,
            "price": {
                "pointsAmount": 5000,
                "priceListId": "priceListClub"
            }
        }
    ]
}
    </pre>
</details>

<details>
    <summary>Clique para ver o Response</summary>
    <pre>
        {
            "code": "INTERNAL_SERVER_ERROR",
            "message": "Internal Server Error",
            "details": [
                "Internal Server Error"
            ]
        }
    </pre>
<details>
