# Como inicializar as dependências do order-flight

O order-flight tem as seguintes dependências externas:

- partners config flight
- Oracle Database

Partners Config Flight:
O order-flight utiliza a biblioteca interna da Livelo, chamada: Partners Config Flight, que é instalada e importada no projeto para buscar a URL do connector do parceiro.

Oracle Database:
Para inicialização do MS e perfeito funcionamento do order-flight é preciso que a oracle database esteja UP, para isso você pode consultar as seguintes documentações interna da Livelo.

- [Configurar back-end Spring Boot para testes locais](https://livelo.atlassian.net/wiki/spaces/TV/pages/162824953/Configurar+back-end+Spring+Boot+para+testes+locais)
- [Oracle Database Docker para desenvolvimento local (instalação manual)](https://livelo.atlassian.net/wiki/spaces/TV/pages/162824953/Configurar+back-end+Spring+Boot+para+testes+locais)

Após seguir os passos da documentação você já vai estar quase pronto para rodar o MS, agora você só precisa executar um script SQL para garantir seu acesso ao banco.

```sql
alter session set "_ORACLE_SCRIPT"=true;
CREATE USER order_flight IDENTIFIED BY order_flight;
GRANT CONNECT TO order_flight;
GRANT CONNECT, RESOURCE, DBA TO order_flight;
GRANT UNLIMITED TABLESPACE TO order_flight;
```
