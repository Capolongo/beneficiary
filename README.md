# MS - register-beneficiary
## Essa API foi elaborado pensando num projeto em ambiente de trabalho 
- Terá padrão de projeto seguindo as camadas
    - Controller
    - Service
    - Repository
    - Entities
    - DTO
    - Enums
    - Exception
    - Mappers
    - Configure
- Terá autenticação simples, mas num projeto teria que avaliar se a API seria publica(sem autorização) ou privada(Com autorição Oaut2, ou outras ...)
- Terá tratamentos de exceção, descrito abaixo
- Terá evidencias de testes, Swaggger, Fluxo do sistema macro
- Testes unitarários, com cobertura acima de 80%, foi evidenciado com print está dentro do diretório resource/evidencias/coverage.png
- Base de dados, foi evidenciado com print está dentro do diretório resource/evidencias/baseDados.png e dataBase.png
- Banco de dados H2
    - url: http://localhost:8080/h2-console/login.do?jsessionid=56e79ee6ddce0174615bdd7b71ec1ff4
    - Usuário: beneficiary
    - senha: 123

## Subir aplicação local
- Foi adicionado o arquivo settings.xml dentro do diretório resource/setting/settings.xml
- Ter instalado a JDK 21, pra poder conseguir testar local
- Ter o maven mais recente
- Pra baixar as dependencias rodar o comando - mvn clean install
- Tendo tudo isso só executar o BeneficiaryApplication e testar local
- Pra facitar foi adicionado a collection do postman, com todos os endpoints

## Foi feito todas os items que foi pedido como requisito e outros descrito abaixo
- Cadastrar um beneficiário junto com seus documentos;
- Listar todos os beneficiários cadastrados;
- Listar todos os documentos de um beneficiário a partir de seu id;
- Atualizar os dados cadastrais de um beneficiário;
- Remover um beneficiário.
- Foi feito a autenticaçao por basic auth, tendo que passar usuario(beneficiary) e a senha(123);
- Foi feito testes unitários, tendo a cobertura superior aos 80%
- Foi incluido logs pra poder ter rastreabilidade, se caso der alguma falha, obtendo pelo dynatrace, grafana, ...
- Foi feito com banco de dados H2;
- Foi feito swagger, está no diretório resource dentro de swagger com o arquivo resources/swagger/beneficiarySwagger.yaml;
- Foi feito tratamento de exceção, incluindo alguns erros funcional, descrito abaixo;
- Foi feito evidencias funcionais, descrito abaixo;
- Foi incluido a colection do postman, pra facilitar dentro do diretório resources/collectionPostman;
- Foi utilizado variavel de ambiente na parte SecurityConfig, se caso for deixar num ambiente na nuvem, cofre de senhas quando subir em outros ambientes.
- Foi utilizado também Java 21, com spring 3;
- Foi utilizado pra montar a conversão de DTO x Entidades com mapstruct;
- Utilizei os campos de entrada em inglês e as entidades mantendo o padrão da modelagem em portugues;
- Foi montado um diagrama, contendo o fluxo de uma forma que ficasse simpes dentro do diretório resources/desenhoTecnico/fluxo.png

## Evidencias funcionais na pasta resources/evidencias
- Cadastrando o beneficiário
  - Cadastro com sucesso
  - Error funcional
  - Não autorizado
- Alterando o beneficiário
  - Alteração com sucesso
  - Error funcional
  - Não autorizado
- Consultando todos os beneficiários
  - Consulta com sucesso
  - Error funcional
  - Não autorizado
- Consultando os documentos pelo id do beneficiário
  - Consulta com sucesso
  - Error funcional
  - Não autorizado
- Removendo o beneficiário
  - Remoção com sucesso
  - Error funcional
  - Não autorizado
- Vai ter evindência de base de dados
- Vai ter evidência de cobertura de testes
- Vai ter evidência do data base
- Vai ter evidência do projeto estartado

## Tratamento de exceções
- No cadastro tem dois tipos de documentos: RG/CPF, se caso passar outro tipo vai dar erro funcional;
- Na alteração se caso o id do benefiário, não estiver na base de dados, dará um erro funcional;
- Na consulta de todos os beneficiários, se caso não tiver nada na base dará uma mensagem que nao existe nada na base;
- Na consulta dos documentos pelo id do beneficiário, se caso o id não tiver na base dará um erro funcional;
- Quando for remover um beneficiário, se caso o id não existir na base de dados, dará um erro funcional;