# Order Flight - Shipment Options

API responsável por retornar uma lista de opções de envio, buscando pelo id do pedido e postalCode

## Endpoint

`GET` /v1/orders/{id}/shipment-options/{postalCode}


## Contexto

Este endpoint retornará uma lista de opções de envio, passando por parâmetro o id do pedido e a o postal code.

## Regras:


- O id do pedido passado como parâmetro deve existir na base. Se existir vai retornar uma resposta de entrega eletrônica para todos os pedidos. Se não existir, irá retornar uma exceção.


## Exemplos de request

1. Retorno da lista:

`GET` /v1/orders/lf261/shipment-options/1234

<details>
    <summary>Clique para ver o retorno</summary>
    <pre>
{
  "shipmentOptions": [
    {
      "id": "lf261",
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