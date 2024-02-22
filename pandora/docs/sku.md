# Order Flight - Shipment Payment Options

API responsável por retornará o sku, passando o id

## Endpoint

`GET` /v1/skus/{id}


## Contexto

Este endpoint retornará o response do sku, passando por id

## Regras:

- Deve ser passado o `id` como parâmetro na requisição, por exemplo, `id=1234`.


## Exemplos de request

1. Retorno da lista:

`GET` /v1/skus/1234

<details>
    <summary>Clique para ver o retorno</summary>
    <pre>
{
  "id": "CVCFLIGHT",
  "description": "CVCFLIGHT",
  "available": true,
  "currency": "PTS",
  "salePrice": 18200,
  "listPrice": 18200,
  "quantity": 1,
  "commerceItemId": "ci19870698620961"
}
</pre>
</details>