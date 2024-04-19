# Order Flight - Payment Installment Options

API responsável por retornar uma lista de opções de parcelamento de pagamento, buscando pelo id do pedido e id do pagamento

## Endpoint

`GET` /orders/{id}/payment-options/{paymentOptionId}/installment-options


## Contexto

Este endpoint retornará uma lista de opções de parcelamento de pagamento, passando por parâmetro o id do pedido e id do pagamento

## Regras:

O id do pedido passado como parâmetro deve existir na base. Se existir vai retornar uma resposta fixa para todos os pedidos. Se não existir, irá retornar uma exceção.


## Exemplos de request

1. Retorno da lista:

`GET` /orders/lf261/payment-options/1234/installment-options

<details>
    <summary>Clique para ver o retorno</summary>
    <pre>
{
  "installmentOptions": [
    {
      "id": "12345",
      "currency": "PTS",
      "interest": 0,
      "parcels": 1,
      "amount": 38500
    }
  ]
}
</pre>
</details>