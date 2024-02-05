# Como mockas as dependências do order-flight

O order-flight tem as seguintes dependências externas:

- connector-partner-[partnerCode]

Para ambas as dependências, existem mocks que podem ser usados para testes. Os mocks estão no projeto [Livelo Viagens Mocks](https://stash.livelo.intranet/projects/LIVMOCKS/repos/livelo-viagens-mocks/browse) que já está deployado em uat. O endpoing base do mock é http://api.k8s.uat.livelo.intranet/mocks/livelo-viagens-mocks/ e para usá-los no ambiente basta realizar os seguintes apontamentos:

