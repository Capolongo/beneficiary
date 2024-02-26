# Order Flight - Process Order

Listener responsável consumir a Queue e processar todos os pedidos.

## Queue

`Queue:` order-flight.flight.commands.order.getconfirmation

<details>
	<summary>Clique para ver o retorno da fila</summary>
	<pre>
		{
			"id": "lf1213",
			"commerceOrderId": "o12313"
		}
   </pre>
</details>

> Observação: Apenas pedidos com `statusCode=LIVPNR-1007` vão ser inseridos
> nessa Queue

## Contexto

Este ouvinte permanecerá atento à fila em busca de pedidos disponíveis para processamento. Ao consumir um pedido, será acionado o fluxo de processamento e a ordem será tentada de ser processada. No fim do processamento, o contador na tabela `processCounter.counter` será incrementado em +1.

Se o contador `processCounter.counter` atingir ou exceder `(order.maxProcessCountFailed)` tentativas, o status de falha `LIVPNR-1014` será registrado.

## Regras:

Para iniciar o processamento, é fundamental que as seguintes condições sejam atendidas:

- A ordem deve existir na base de dados.
- O status da ordem precisa ser `LIVPNR-1007`.
- O contador de tentativas de processamento da ordem deve ser inferior a `(order.maxProcessCountFailed)`.

> Observação: Variável `order.maxProcessCountFailed` do `application.yml`, tem o valor default de 48.
