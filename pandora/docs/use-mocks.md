# Como mockas as dependências do order-flight

O order-flight tem as seguintes dependências externas:

- connector-flight-order-{partner}
- partners-config-flight

Para satisfazer as dependências, existem mocks que podem ser usados para testes. Os mocks estão no projeto [Livelo Viagens Mocks](https://stash.livelo.intranet/projects/LIVMOCKS/repos/livelo-viagens-mocks/browse) que já está deployado em uat. O endpoint base do mock é http://api.k8s.uat.livelo.intranet/mocks/livelo-viagens-mocks/ e para usá-los no ambiente basta realizar os seguintes apontamentos:

- partners-config-flight: no application.yml do ambiente desejado, alterar o `client.partnersconfigflight.endpoint` para `http://api.k8s.uat.livelo.intranet/mocks/livelo-viagens-mocks/partners-config-flight`;
- connector-flight-order-{partner}: é a URL retornada pelo getWebhook no partners-config-flight, que pode ser alterada no cadastro dos webhooks dos parceiros;
