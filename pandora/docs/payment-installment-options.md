# Order Flight - Payment Installment Options

API responsável por retornar uma lista de opções de parcelamento de pagamento, buscando pelo id e parcelamento de pagamento

## Endpoint

`GET` /orders/{id}/payment-options/{paymentOptionId}/installment-options


## Contexto

Este endpoint retornará uma lista de opções de parcelamento de pagamento, passando por parametro o id e pagamento de parcelamento

## Regras:

- Deve ser passado o `id` como parâmetro na requisição, por exemplo, `id=951661608301` e `paymentOptionId` como parâmetro na requisição, por exemplo, `paymentOptionId=1234`.


## Exemplos de request

1. Retorno da lista:

`GET` /orders/951661608301/payment-options/1234/installment-options

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