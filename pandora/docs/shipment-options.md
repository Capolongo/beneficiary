# Order Flight - Shipment Options

API responsável por retornar uma lista de opções de envio, buscando pelo id e opções de envio

## Endpoint

`GET` /v1/orders/{id}/shipment-options/{postalCode}


## Contexto

Este endpoint retornará uma lista de opções de envio, passando por parametro o id e a opção de envio.

## Regras:

- Deve ser passado o `id` como parâmetro na requisição, por exemplo, `id=951661608301` e `shipmentOptionId` como parâmetro na requisição, por exemplo, `postalCode=1234`.


## Exemplos de request

1. Retorno da lista:

`GET` /v1/orders/951661608301/shipment-options/1234

<details>
    <summary>Clique para ver o retorno</summary>
    <pre>
{
  "shipmentOptions": [
    {
      "id": "12345",
      "currency": "PTS",
      "description": "Sem entrega física",
      "price": 0.1,
      "type": "Eletrônica",
      "items": [
        "CVCFLIGHT",
        "CVCFLIGHTTAX"
      ],
      "commerceItems": [
        {
          "deliveryDate": "2024-02-20T14:32:51.016+00:00",
          "commerceItems": [
            {
              "id": "CVCFLIGHT",
              "partnerOrderId": "951661608301",
              "commerceItemId": "ci19870698620961",
              "partnerOrderLinkId": "CVC-o1240272701"
            }
          ]
        }
      ]
    }
  ]
}
</pre>
</details>