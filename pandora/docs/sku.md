# Order Flight - Shipment Payment Options

API responsável por retornará o sku, passando por parêmtro o id da SKU e o currency. O commerceItemId é opcional.

## Endpoint

`GET` /v1/skus/{id}?commerceItemId=1234&currency=PTS


## Contexto

Este endpoint retornará o response do sku, passando por parâmetro de acordo com os querys param

## Regras:

- Deve ser passado como parametro patch o `id` e por parametro query `commerceItemId`, `currency`, e  como parâmetro na requisição, por exemplo, `id=CVCFLIGHT`, `commerceItemId=1234` e `currency=PTS`.
- commerceItemId enviado - busca ele na base e retorna os valores de acordo com o que está na base. Se não encontrou, deve retornar uma exceção.
- commerceItemId não enviado - retorna um payload padrão de acordo com o exemplo 2, destacado abaixo.


## Exemplos de request

1. Retorno da lista se caso enviar o commerceItemId:

`GET` /v1/skus/CVCFLIGHT?commerceItemId=1234&currency=PTS

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

2. Retorno da lista se caso não enviar commerceItemId:

`GET` /v1/skus/CVCFLIGHT?currency=PTS

<details>
    <summary>Clique para ver o retorno</summary>
    <pre>
{ 
  "id": "CVCFLIGHT", 
  "description": "CVCFLIGHT", 
  "available": true, 
  "currency": "PTS", 
  "salePrice": 1, 
  "listPrice": 1, 
  "quantity": 1 
}
</pre>
</details>