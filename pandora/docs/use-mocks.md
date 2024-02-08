# Como mockas as dependências do order-flight

O order-flight tem as seguintes dependências externas:

- connector-flight-order-cvc

Para satisfazer as dependências, existem mocks que podem ser usados para testes. Os mocks estão no projeto [Livelo Viagens Mocks](https://stash.livelo.intranet/projects/LIVMOCKS/repos/livelo-viagens-mocks/browse) que já está deployado em uat. O endpoint base do mock é http://api.k8s.uat.livelo.intranet/mocks/livelo-viagens-mocks/

# Como utilizar os mocks do connector parceiro

Para utilizar os mocks do connector parceiro, siga estas etapas:

1. Utilize a rota interna [POST] createOrder para criar uma ordem na base com algumas modificações no payload de criação.
   Você deve alterar os seguintes parâmetros do objeto usuário que fica no caminho `items.travelInfo.paxs[0]`, por exemplo:

   ```json
   {
     "firstName": "LIVELO",
     "lastName": "MOCK",
     "phoneNumber": "999991007"
   }
   ```

Quando `firstName` é "LIVELO" e `lastName` é "MOCK", você deve testar todos os status que não sejam de falha, determinando apenas os últimos 4 dígitos do `phoneNumber`. No exemplo acima, teríamos o status `LIVPNR-1007`.

2. Para testar os cenários de falha:

```json
  "firstName": "LIVELO",
  "lastName": "ERROR",
  "phoneNumber": "999999001",
```

3. Para testar o cenário de Internal Server Error:

```json
  "firstName": "ERROR",
  "lastName": "ERROR",
```

<details>
  <summary>Clique para ver o payload de criação</summary>
    <pre>
    {
        "commerceOrderId": "o1002",
        "partnerOrderId": "5010",
        "partnerCode": "cvc",
        "submittedDate": "2023-09-21T14:49:01",
        "expirationDate": "2023-09-21T14:49:01",
        "channel": "I",
        "tierCode": "default",
        "originOrder": "site",
        "customerIdentifier": "105181",
        "transactionId": "uuid",
        "currentStatus": {
            "code": "LIVPNR-1006",
            "description": "INITIAL",
            "partnerCode": "1",
            "partnerDescription": "create",
            "partnerResponse": null,
            "statusDate": "2023-09-21T14:49:01"
        },
        "statusHistory": [
            {
                "code": "LIVPNR-1006",
                "description": "INITIAL",
                "partnerCode": "1",
                "partnerDescription": "create",
                "partnerResponse": null,
                "statusDate": "2023-09-21T14:49:01"
            }
        ],
        "price": {
            "amount": 1999.99,
            "partnerAmount": 1999.99,
            "pointsAmount": 20000,
            "priceListId": "priceListClub",
            "accrualPoints": 1000.00,
            "ordersPriceDescription": [
                {
                    "amount": 1500.00,
                    "pointsAmount": 130000,
                    "type": "BY_ADULT",
                    "description": "preço por adulto"
                },
                {
                    "amount": 499.99,
                    "pointsAmount": 20000,
                    "type": "TOTAL_TAX",
                    "description": "preço taxas"
                }
            ]
        },
        "items": [
            {
                "commerceItemId": "ci16238303923791",
                "skuId": "cvc_flight",
                "productId": "flight",
                "externalCoupon": "cupon10",
                "quantity": 1,
                "price": {
                    "listPrice": 1500.00,
                    "amount": 1500.00,
                    "pointsAmount": 15000,
                    "accrualPoints": 1500,
                    "partnerAmount": 1500.00,
                    "priceListId": "priceListClub",
                    "priceRule": "cpp club cvc"
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
                            "firstName": "LIVELO",
                            "lastName": "MOCK",
                            "phoneNumber": "999991007",
                            "gender": "M",
                            "birthDate": "1995-10-10",
                            "email": "email@gmail.com",
                            "areaCode": "54",
                            "document": [
                                {
                                    "documentNumber": "12332112312",
                                    "type": "CPF",
                                    "issueDate": "12-12-2023",
                                    "issuingCountry": "Brasil",
                                    "expirationDate": "01-01-2024",
                                    "residenceCountry": "Brasil"
                                }
                            ]
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
                                "timeToWait": 10,
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
                                "timeToWait": 10,
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
                "commerceItemId": "ci13406264327442",
                "skuId": "cvc_flight_tax",
                "productId": "flight",
                "externalCoupon": "cupon10",
                "quantity": 1,
                "price": {
                    "listPrice": 499.99,
                    "amount": 499.99,
                    "pointsAmount": 5000,
                    "accrualPoints": 500,
                    "partnerAmout": 499.99,
                    "priceListId": "priceListClub",
                    "priceRule": "cpp club cvc"
                }
            }
        ]
    }
    </pre>
</details>

4. Após a criação da ordem com as modificações mencionadas acima, basta bater no endpoint de confirmação de ordem passando o Id retornado na criação da ordem para obter os status esperados.

Para mais detalhes, consulte a [documentação completa aqui](https://livelo.atlassian.net/l/cp/KmSspck3).

# Como utilizar os mocks de confirmação de ordem

Você deve bater a url do MOCK [POST] http://api.k8s.uat.livelo.intranet/mocks/livelo-viagens-mocks/ com o endpoint /order-flight/v1/orders/{id}/confirmation

E o id determina qual tipo de ordem com determinado status você vai obter, os possíveis ID's são:

- lf200 = LIVPNR-1019
- lf206 = LIVPNR-1007
- lf400 = LIVPNR-9001
- lf400c = LIVPNR-1014
- lf500 = Internal Server Error
