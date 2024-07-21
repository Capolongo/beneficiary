# Beneficiário 

## Foi feito todas os items que foi pedido como requisito
- Cadastrar um beneficiário junto com seus documentos;
- Listar todos os beneficiários cadastrados;
- Listar todos os documentos de um beneficiário a partir de seu id;
- Atualizar os dados cadastrais de um beneficiário;
- Remover um beneficiário.
- Foi feito a autenticaçao por basic auth, tendo que passar usuário e senha
- Foi feito testes unitários, tendo a cobertura superior aos 80%
- Foi incluido logs pra poder ter rastreabilidade, se caso der alguma falha, obtendo pelo dynatrace, grafana, ...
- Foi feito com banco de dados H2;
- Foi feito swagger, está no diretório resource dentro de swagger com o arquivo recipientSwagger.yaml;
- Foi feito tratamento de exceção, incluindo alguns erros funcional, descrito abaixo;
- Foi feito evidencias funcionais, descrito abaixo;
- Foi incluido a colection do postman, pra facilitar;
- Foi utilizado variavel de ambiente na parte SecurityConfig, se caso for deixar num ambiente na nuvem, cofre de senhas quando subir em outros ambientes.
- Foi utilizado também Java 21, com spring 3;
- Foi utilizado pra montar a conversão de DTO x Entidades com mapstruct;
- Utilizei os campos de entrada em inglês e as entidades mantendo o padrão da modelagem em portugues;

## Inclui evidencias funcionais na pasta resource dentro do arquivo de envidências
- Cadastrando o beneficiário
- Alterando o beneficiário
- Consultando todos os beneficiários
- Consultando os documentos pelo id do beneficiário
- Removendo o beneficiário

## Foi feito tratamento de exceção
- No cadastro tem dois tipos de documentos: RG/CPF, se caso passar outro tipo vai dar erro funcional;
- Na alteração se caso o id do benefiário, não estiver na base de dados, dará um erro funcional;
- Na consulta de todos os beneficiários, se caso não tiver nada na base dará uma mensagem que nao existe nada na base;
- Na consulta dos documentos pelo id do beneficiário, se caso o id não tiver na base dará um erro funcional;
- Quando for remover um beneficiário, se caso o id não existir na base de dados, dará um erro funcional;





