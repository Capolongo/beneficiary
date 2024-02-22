# Order Flight - Shipment Payment Options

API responsável por retornar uma lista de opções de pagamento, buscando pelo id

## Endpoint

`GET` /v1/orders/{id}/shipment-options/{shipmentOptionId}/payment-options


## Contexto

Este endpoint retornará uma lista de opções de pagamento, passando por parametro o id e a opção de pagamento.

## Regras:

- Deve ser passado o `id` como parâmetro na requisição, por exemplo, `id=951661608301` e `shipmentOptionId` como parâmetro na requisição, por exemplo, `shipmentOptionId=1234`.


## Exemplos de request

1. Retorno da lista:

`GET` /v1/orders/951661608301/shipment-options/1234/payment-options

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