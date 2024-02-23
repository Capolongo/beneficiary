# Order Flight - Shipment Payment Options

API responsável por retornar uma lista de opções de pagamento, buscando pelo id

## Endpoint

`GET` /v1/orders/{id}/shipment-options/{shipmentOptionId}/payment-options


## Contexto

Este endpoint retornará uma lista de opções de pagamento, passando por parametro o id e a opção de pagamento.

## Regras:


- O id do pedido passado como parâmetro deve existir na base. Se existir vai retornar uma resposta fixa para todos os pedidos. Se não existir, irá retornar uma exceção.


## Exemplos de request

1. Retorno da lista:

`GET` /v1/orders/lf261/shipment-options/12345/payment-options

<details>
    <summary>Clique para ver o retorno</summary>
    <pre>
{
  "paymentOptions": [
    {
      "id": "12345",
      "name": "default",
      "description": "default"
    }
  ]
}
</pre>
</details>