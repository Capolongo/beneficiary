# Order Flight - Payment Installment Options

API responsável por atualizar o status

## Endpoint

`PATCH` /v1/orders/{id}/status


## Contexto

Este endpoint atualizará na base de dados o status enviado.

## Regras:

Para poder atualizar na base, é fundamental que as seguintes condições sejam atendidas:

- O id deve existir na base de dados, caso não existir, deve retornar erro.
- Atualizar o pedido somente em caso: 
  1. Pedido estiver com o status INITIAL e o novo status NÃO for PROCESSING
  2. Pedido não estiver com o status de CANCELED ou COMPLETED


## Exemplos de request

1. Sucess:

`PATCH` /v1/orders/lf382/status

<details>
    <summary>Clique para ver o request</summary>
    <pre>
{
  "orderId": "o12596228",
  "items": [
    {
      "id": "CVCFLIGHT",
      "commerceItemId": "ci15026231342651",
      "status": {
        "code": "LIVPNR-1027",
        "message": "CANCELLED",
        "details": "Item reprocessado manualmente"
      },
      "reason": "Item cancelado automaticamente",
      "user": "CANCEL"
    },
    {
      "id": "CVCFLIGHTTAX",
      "commerceItemId": "ci19531291232152",
      "status": {
        "code": "LIVPNR-1027",
        "message": "CANCELLED",
        "details": "Item reprocessado manualmente"
      },
      "reason": "Item cancelado automaticamente",
      "user": "CANCEL"
    }
  ]
}
</pre>
</details>

<details>
    <summary>Clique para ver o response</summary>
    <pre>
{
    "id": "lf30",
    "commerceOrderId": "o1002",
    "partnerOrderId": "5010",
    "partnerCode": "cvc",
    "submittedDate": "2023-09-21T14:49:01",
    "expirationDate": "2023-09-21T14:49:01",
    "transactionId": "uuid",
    "status": {
        "code": "LIVPNR-9001",
        "description": "CANCELED",
        "details": null
    },
    "price": {
        "amount": 1999.99,
        "pointsAmount": 20000
    },
    "items": [
        {
            "commerceItemId": "ci16238303923791",
            "skuId": "cvc_flight",
            "productId": "flight",
            "quantity": 1,
            "externalCoupon": "cupon10",
            "productType": "flight",
            "price": {
                "amount": 1500,
                "pointsAmount": 15000
            },
            "travelInfo": {
                "type": "ROUND_TRIP",
                "reservationCode": "YD5WK",
                "adultQuantity": 2,
                "childQuantity": 0,
                "babyQuantity": 0,
                "typeClass": "ECONOMIC",
                "voucher": "link.voucher.com.br",
                "paxs": [
                    {
                        "type": "ADULT",
                        "firstName": "LIVELO",
                        "lastName": "MOCK",
                        "birthDate": "1995-10-10",
                        "document": [
                            {
                                "id": 30,
                                "documentNumber": "12332112312",
                                "type": "CPF",
                                "issueDate": "12-12-2023",
                                "issuingCountry": "Brasil",
                                "expirationDate": "01-01-2024",
                                "residenceCountry": "Brasil"
                            }
                        ],
                        "email": "email@gmail.com",
                        "areaCode": "54",
                        "phone": "999991030"
                    }
                ]
            },
            "segments": [
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
                            "timeToWait": "10",
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
                            "type": "BAG",
                            "description": "Bagagem despachada"
                        },
                        {
                            "type": "HAND",
                            "description": "Bagagem de mão"
                        }
                    ],
                    "cancelationRules": [
                        {
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        },
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
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
                            "timeToWait": "10",
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
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        },
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
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
            "commerceItemId": "ci13406264327442",
            "skuId": "cvc_flight_tax",
            "productId": "flight",
            "quantity": 1,
            "externalCoupon": "cupon10",
            "productType": "flight",
            "price": {
                "amount": 499.99,
                "pointsAmount": 5000
            },
            "travelInfo": null,
            "segments": []
        }
    ]
}
</pre>
</details>

1. Id não encontrado na base:

`PATCH` /v1/orders/lf382/status

<details>
    <summary>Clique para ver o request</summary>
    <pre>
{
  "orderId": "o12596228",
  "items": [
    {
      "id": "CVCFLIGHT",
      "commerceItemId": "ci15026231342651",
      "status": {
        "code": "LIVPNR-1027",
        "message": "CANCELLED",
        "details": "Item reprocessado manualmente"
      },
      "reason": "Item cancelado automaticamente",
      "user": "CANCEL"
    },
    {
      "id": "CVCFLIGHTTAX",
      "commerceItemId": "ci19531291232152",
      "status": {
        "code": "LIVPNR-1027",
        "message": "CANCELLED",
        "details": "Item reprocessado manualmente"
      },
      "reason": "Item cancelado automaticamente",
      "user": "CANCEL"
    }
  ]
}
</pre>
</details>

<details>
    <summary>Clique para ver o response</summary>
    <pre>
{
    "id": "lf30",
    "commerceOrderId": "o1002",
    "partnerOrderId": "5010",
    "partnerCode": "cvc",
    "submittedDate": "2023-09-21T14:49:01",
    "expirationDate": "2023-09-21T14:49:01",
    "transactionId": "uuid",
    "status": {
        "code": "LIVPNR-9001",
        "description": "CANCELED",
        "details": null
    },
    "price": {
        "amount": 1999.99,
        "pointsAmount": 20000
    },
    "items": [
        {
            "commerceItemId": "ci16238303923791",
            "skuId": "cvc_flight",
            "productId": "flight",
            "quantity": 1,
            "externalCoupon": "cupon10",
            "productType": "flight",
            "price": {
                "amount": 1500,
                "pointsAmount": 15000
            },
            "travelInfo": {
                "type": "ROUND_TRIP",
                "reservationCode": "YD5WK",
                "adultQuantity": 2,
                "childQuantity": 0,
                "babyQuantity": 0,
                "typeClass": "ECONOMIC",
                "voucher": "link.voucher.com.br",
                "paxs": [
                    {
                        "type": "ADULT",
                        "firstName": "LIVELO",
                        "lastName": "MOCK",
                        "birthDate": "1995-10-10",
                        "document": [
                            {
                                "id": 30,
                                "documentNumber": "12332112312",
                                "type": "CPF",
                                "issueDate": "12-12-2023",
                                "issuingCountry": "Brasil",
                                "expirationDate": "01-01-2024",
                                "residenceCountry": "Brasil"
                            }
                        ],
                        "email": "email@gmail.com",
                        "areaCode": "54",
                        "phone": "999991030"
                    }
                ]
            },
            "segments": [
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
                            "timeToWait": "10",
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
                            "type": "BAG",
                            "description": "Bagagem despachada"
                        },
                        {
                            "type": "HAND",
                            "description": "Bagagem de mão"
                        }
                    ],
                    "cancelationRules": [
                        {
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        },
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
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
                            "timeToWait": "10",
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
                            "type": "2",
                            "description": "Cancelamento parcial com menos de 48h"
                        },
                        {
                            "type": "1",
                            "description": "Cancelamento reembolsábel até  2 dias antes"
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
            "commerceItemId": "ci13406264327442",
            "skuId": "cvc_flight_tax",
            "productId": "flight",
            "quantity": 1,
            "externalCoupon": "cupon10",
            "productType": "flight",
            "price": {
                "amount": 499.99,
                "pointsAmount": 5000
            },
            "travelInfo": null,
            "segments": []
        }
    ]
}
</pre>
</details>