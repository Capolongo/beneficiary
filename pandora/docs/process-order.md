# Order Flight - Process Order

API responsável por retornar uma lista de pedidos de um determinado status.

## Endpoint

`GET` /v1/orders/process

## Contexto

Este endpoint retornará uma lista de pedidos de um determinado status que será passado via parâmetro. Além do status,
também existirá paginação via parâmetro, onde você pode escolher a página e a quantidade de linhas. A quantidade de
linhas não pode ser maior que a quantidade configurada na variável `order.orderProcessMaxRows` do `application.yml`.

## Regras:

- Deve ser passado o `statusCode` como parâmetro na requisição, por exemplo, `statusCode=LIVPNR-1007`.
- A página pode ser escolhida através do parâmetro `page`, por default o valor será a primeira página (`page=0`).
- A quantidade de linhas pode ser escolhida através do parâmetro `rows`, e deve ser menor do que o valor da
  variável `order.orderProcessMaxRows` do `application.yml`, que é também o valor default.

## Mocks

No processamento de ordem existem algumas opções de retornos nos mocks.

1. No primeiro é o mock default, caso não seja enviado nenhum parâmetro.

`GET` /v1/orders/process

<details>
    <summary>Clique para ver o retorno</summary>
    <pre>
{
    "orders": [
        {
            "id": "lf121",
            "commerceOrderId": "o1231"
        },
        {
            "id": "lf122",
            "commerceOrderId": "o1232"
        },
        {
            "id": "lf123",
            "commerceOrderId": "o1233"
        },
        {
            "id": "lf124",
            "commerceOrderId": "o1234"
        },
        {
            "id": "lf125",
            "commerceOrderId": "o1235"
        }
    ],
    "page": 1,
    "rows": 100,
    "total": 13,
    "totalPages": 1
}
    </pre>
</details>

2. O segundo cenário tem algumas opções da quantidade de linhas que será retornado.

- O parâmetro `statusCode` deve ser igual a `LIVPNR-1007`
- O parâmetro `page` deve ser igual a `1`
- O parâmetro `rows` pode ser `12`, `25`, `50` ou `100`, dependendo da quantidade de linhas desejada

`GET` /v1/orders/process?statusCode=LIVPNR-1007&page=1&rows=12

<details>
    <summary>Clique para ver o retorno</summary>
    <pre>
{
    "orders": [
        {
            "id": "lf121",
            "commerceOrderId": "o1231"
        },
        {
            "id": "lf122",
            "commerceOrderId": "o1232"
        },
        {
            "id": "lf123",
            "commerceOrderId": "o1233"
        },
        {
            "id": "lf124",
            "commerceOrderId": "o1234"
        },
        {
            "id": "lf125",
            "commerceOrderId": "o1235"
        },
        {
            "id": "lf126",
            "commerceOrderId": "o1236"
        },
        {
            "id": "lf127",
            "commerceOrderId": "o1237"
        },
        {
            "id": "lf128",
            "commerceOrderId": "o1238"
        },
        {
            "id": "lf129",
            "commerceOrderId": "o1239"
        },
        {
            "id": "lf121",
            "commerceOrderId": "o1231"
        },
        {
            "id": "lf121",
            "commerceOrderId": "o1231"
        },
        {
            "id": "lf121",
            "commerceOrderId": "o1231"
        },
        {
            "id": "lf1210",
            "commerceOrderId": "o12310"
        },
        {
            "id": "lf1211",
            "commerceOrderId": "o12311"
        },
        {
            "id": "lf1212",
            "commerceOrderId": "o12312"
        },
        {
            "id": "lf1213",
            "commerceOrderId": "o12313"
        }
    ],
    "page": 1,
    "rows": 12,
    "total": 13,
    "totalPages": 2
}
</pre>
</details>