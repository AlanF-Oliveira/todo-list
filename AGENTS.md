# Diretrizes do projeto

## 1. Visão geral

Este repositório contém uma aplicação de lista de tarefas com duas superfícies:

- Um backend/aplicação de terminal em Java, iniciado por `org.alan.todolist.TodoList`.
- Uma interface web estática em `frontend/`, executada abrindo `frontend/index.html` no navegador.

No fluxo Java, tarefas possuem nome, descrição, data/hora final, prioridade de 1 a 5,
categoria, status (`TODO`, `DOING` ou `DONE`) e uma ou mais configurações de alarme.
O menu de terminal permite criar, listar, filtrar por categoria/prioridade/status,
alterar e excluir tarefas individualmente ou em conjunto. Alarmes pendentes são
verificados ao retornar ao menu, e tarefas concluídas não disparam alarmes.

Na interface web, é possível criar, editar e excluir tarefas, configurar um alarme,
visualizar os dados e alterar o status de várias tarefas selecionadas. Os dados web
são persistidos no `localStorage` do navegador. O repositório Java mantém os dados em
memória durante a execução; não foi encontrada uma integração entre essa camada e a
interface web.

O `README.md` informa ordenação por prioridade no cadastro. Essa ordenação é
implementada no serviço Java; a implementação web atual adiciona tarefas ao final da
lista. Essa diferença deve ser tratada como comportamento existente até que uma
especificação de mudança seja aprovada.

## 2. Stack e estrutura

- Java 8 (conforme `README.md`).
- Gradle, com plugins `java`, `application` e `groovy`.
- Groovy 3.0.19 e Spock 2.3 para testes.
- HTML, JavaScript e Bootstrap 5.3 via CDN na interface web.

Estrutura principal:

```text
src/main/java/org/alan/todolist/
├── TodoList.java                 # ponto de entrada da aplicação Java
├── model/                        # Todo, Alarm e Status
├── repository/                   # armazenamento em memória
├── service/                      # regras e operações de tarefas
└── terminal/                     # menu interativo
src/test/groovy/                  # especificação Spock do serviço
frontend/
├── index.html                    # formulário e listagem web
├── js/script.js                  # estado, CRUD e localStorage
└── css/style.css                 # atualmente vazio
build.gradle                      # configuração Gradle
settings.gradle                   # nome do projeto
README.md                         # documentação existente
```

Os diretórios gerados `build/`, `.gradle/` e `bin/` não fazem parte do código-fonte
mantido pelo projeto e estão ignorados pelo Git conforme `.gitignore`.

## 3. Execução e testes

Na raiz do projeto:

```bash
./gradlew run
```

Esse comando inicia o menu interativo Java. Também é possível executar diretamente
a classe `org.alan.todolist.TodoList` pela IDE.

Para usar a interface web, abra `frontend/index.html` em um navegador. Ela depende
do Bootstrap carregado pela CDN.

Para executar os testes:

```bash
./gradlew test
```

Os testes existentes são testes Spock da `TodoService`, cobrindo cadastro, listagem,
filtros, exclusão, atualização e alarmes. Não foi encontrada configuração de testes
automatizados para `frontend/`.

## 4. Convenções observadas

- O código Java usa pacotes sob `org.alan.todolist` e separa modelo, repositório,
  serviço e interação de terminal.
- A aplicação Java usa `LocalDateTime` para datas e `DateTimeFormatter` com o padrão
  `dd/MM/yyyy HH:mm` no terminal e na representação textual de tarefas.
- Status são representados pelo enum `Status`; prioridades válidas ficam entre 1 e 5.
- O serviço concentra operações de negócio e delega armazenamento ao
  `TodoRepository`.
- Os testes ficam em `src/test/groovy`, usam Spock e seguem o padrão `given/when/then`.
- O JavaScript da interface usa funções e estado em escopo de arquivo, manipula o DOM
  diretamente e serializa tarefas no `localStorage`.
- Não há configuração de linter, formatter ou análise estática identificada no
  repositório. Preserve o estilo local ao alterar arquivos até que uma convenção
  formal seja especificada.

## 5. Restrições para desenvolvimento e refatoração

- A especificação oficial da refatoração está em [docs/spec.md](./docs/spec.md).
- O plano de execução da refatoração está em [docs/plano.md](./docs/plano.md).
- Antes de alterar código, produza ou atualize uma especificação verificável para o
  comportamento pretendido, seguindo SDD (Spec-Driven Development).
- Preserve as funcionalidades existentes do backend Java e da interface web, salvo
  quando a especificação aprovada determinar explicitamente uma mudança de
  comportamento.
- Não altere contratos, mensagens, persistência web, fluxo do menu, regras de
  prioridade/status ou comportamento de alarmes sem testes e justificativa na
  especificação correspondente.
- Não misture refatorações com novas funcionalidades, correções não relacionadas ou
  mudanças de dependências.
- Restrinja cada alteração ao escopo da especificação. Não modifique arquivos gerados,
  configurações da IDE ou arquivos não relacionados.
- Execute `./gradlew test` após mudanças no backend ou nos testes e confirme que os
  testes existentes continuam passando.
- Não assuma integração entre backend e frontend, persistência no backend ou
  comportamento web não demonstrado pelos arquivos atuais.
- Ao encontrar comportamento ambíguo ou conflito entre documentação e implementação,
  registre a dúvida e solicite uma decisão antes de alterá-lo.
