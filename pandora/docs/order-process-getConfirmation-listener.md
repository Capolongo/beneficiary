# Order Flight - Process Order

Listener responsável pelo processo de consulta de pedidos que estão aguardando a confirmação do parceiro.

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

Este listener permanecerá atento à fila em busca de pedidos disponíveis para processamento. Ao consumir um pedido, será iniciado o fluxo de processamento e a ordem tentará ser processada, podendo falhar durante o processo. No fim do processamento, o contador na tabela `processCounter.counter` será incrementado em +1.

Se o contador `processCounter.counter` atingir ou exceder `(order.maxProcessCountFailed)` tentativas, o status de falha `LIVPNR-1014` será registrado.

## Fluxo

O fluxo de processamento se da inicio no MS [order-flight-scheduler](https://stash.livelo.intranet/projects/LIVTRAVEL/repos/order-flight-scheduler/browse), onde ele busca e adiciona à fila uma quantidade específica de pedidos para processamento. Em seguida, o MS order-flight consome os pedidos da fila de forma individual, realizando diversas validações. Após essas validações, o sistema acessa o webhook getConfirmation do parceiro para atualizar o status do pedido. Caso o status seja LIVPNR-1030, é necessário também atualizar o voucher. Por fim, o sistema registra a atualização do pedido na base de dados e incrementa o contador de processamento.

## Regras:

Para iniciar o processamento, é fundamental que as seguintes condições sejam atendidas:

- A ordem deve existir na base de dados.
- O status da ordem precisa ser `LIVPNR-1007`.
- O contador de tentativas de processamento da ordem deve ser inferior a `(order.maxProcessCountFailed)`.

> Observação: Variável `order.maxProcessCountFailed` do `application.yml`, tem o valor default de 48.

## Diagrama

![Diagrama de fluxo.](images/listener-getCormation-diagram.jpg)