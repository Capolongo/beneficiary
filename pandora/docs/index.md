# Order Flight

Serviço responsável por se comunicar com o connector do parceiro nas compras de passagens aéreas. Esse serviço contempla as seguintes funcionalidades:
- Reservar a passagem aérea;
- Solicitação da confirmação da ordem;

## Resumo

| Campo           |        Valor        |
| --------------- | :-----------------: |
| Namespace       |      livtravel      |
| Scheduler       |         Não         |
| Escalável       |         Sim         |
| OracleDB        |         Sim         |
| MongoDB         |         Não         |
| Redis           |         Não         |
| RabbitMQ        |         Não         |
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

Em seguida é importante estar autenticado na AWS e dentro da pasta do projeto devemos rodar o comando `mvn install`.
O microsserviço está configurado para rodar na porta <b>8080<b> por padrão.

## Endpoint UAT

http://api.k8s.uat.livelo.intranet/order-flight
ou
http://order-flight.livtravel.svc.cluster.local
