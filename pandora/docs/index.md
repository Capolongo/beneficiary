# Order Flight

O serviço 'order-flight' desempenha um papel fundamental no ecossistema de compras de passagens aéreas, indo além da comunicação com os conectores de parceiros. Ele serve para o gerenciamento completo dos pedidos de voos, incorporando as intricadas regras de negócio da Livelo relacionadas a passagens aéreas.

## Resumo

| Campo           |        Valor        |
| --------------- | :-----------------: |
| Namespace       |      livtravel      |
| Scheduler       |         Não         |
| Escalável       |         Sim         |
| OracleDB        |         Sim         |
| MongoDB         |         Não         |
| Redis           |         Não         |
| RabbitMQ        |         Sim         |
| API Pública     |         Não         |
| API Privada     |         Sim         |
| Doc. Atualizada |         Sim         |
| Monitoria       | Dynatrace e Grafana |

## Executar Localmente

Para a configuração do MS em local devemos rodar o comando `git clone ssh://git@stash.livelo.intranet:7999/livtravel/order-flight.git` para baixar na nossa maquina o projeto.

Atenção, você precisa ter o ambiente JAVA configurado em sua máquina:
- Maven;
- Java 21;
- Conexão com oracle database;

Você pode consultar as documentações internas da Livelo para configuração do seu ambiente. [Clique aqui para ver a Documentação](use-dependencies.md).

Em seguida é importante estar autenticado na AWS e dentro da pasta do projeto devemos rodar o comando `mvn install`.
O microsserviço está configurado para rodar na porta <b>8080<b> por padrão.

## Endpoint UAT

http://api.k8s.uat.livelo.intranet/order-flight
ou
http://order-flight.livtravel.svc.cluster.local
