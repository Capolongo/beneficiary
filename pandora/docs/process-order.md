# Order Flight - Process Order

API responsável por retornar uma lista de pedidos de um determinado status.

## Endpoint

`GET` /v1/orders/process

## Contexto

Este endpoint retornará uma lista de pedidos de um determinado status que será passado via parâmetro. Além do status, também existirá paginação via parâmetro, onde você pode escolher a página e a quantidade de linhas. A quantidade de linhas não pode ser maior que a quantidade configurada na variável `order.orderProcessMaxRows` do `application.yml`.  

## Regras:

- Deve ser passado o `statusCode` como parâmetro na requisição, por exemplo, `statusCode=LIVPNR-1007`.
- A página pode ser escolhida através do parâmetro `page`, por default o valor será a primeira página (`page=0`).
- A quantidade de linhas pode ser escolhida através do parâmetro `rows`, e deve ser menor do que o valor da variável `order.orderProcessMaxRows` do `application.yml`, que é também o valor default. 

## Mocks
