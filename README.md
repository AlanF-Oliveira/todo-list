# TODO List

**Autor:** Alan Ferreira Oliveira

Aplicação de lista de tarefas, com backend em Java e interface web.

## Tecnologias

* Java 8
* Gradle
* HTML
* JavaScript
* Bootstrap

## Como executar

```bash
git clone https://github.com/AlanF-Oliveira/todo-list.git
cd todo-list
./gradlew run
```

Ou rode a classe `org.alan.todolist.TodoList` diretamente pela IDE.

Para acessar a interface web, abra o arquivo `index.html` no navegador.

## Sobre a solução

O projeto é organizado em camadas (`model`, `repository`, `service`, `terminal`). Tarefas têm nome, descrição, data de término, prioridade (1-5), categoria e status (TODO/DOING/DONE).

A interface web permite criar, editar e excluir tarefas, além de configurar alarmes para cada tarefa.

Ao cadastrar uma nova tarefa, ela é inserida automaticamente na posição correta da lista, mantendo a ordenação por prioridade.


## Persistência

As tarefas são persistidas no Local Storage. Também é possível selecionar **várias** **tarefas** e alterar seus **status** simultaneamente.

## Alarme de tarefas

Tarefas podem ter alarmes configurados, avisando uma ou mais horas antes do prazo. A cada retorno ao menu, alarmes pendentes são exibidos. Tarefas `DONE` não disparam alarme.


## Testes

Testes com Spock, cobrindo os métodos CRUD da Service, filtros, prioridades,
atualizações, exceções e alarmes.

## Como executar os testes

```bash
./gradlew test
```

## Refatoração Clean Code

As principais correções realizadas no backend foram:

* extração de funções menores no menu, separando seleção de opções, leitura de
  dados, configuração de alarmes e impressão de tarefas;
* remoção de duplicação entre criação e atualização de tarefas e entre filtros;
* substituição de nomes pouco claros e abreviações por nomes autoexplicativos;
* melhoria do encapsulamento do repositório e da coleção de alarmes;
* tratamento explícito de prioridades inválidas, IDs inexistentes e argumentos
  nulos;
* injeção de `TodoRepository` e `Clock`, tornando o serviço mais testável;
* remoção de comentários obsoletos e correção de inconsistências de formatação;
* ampliação dos testes automatizados para casos normais e de borda.

Essas mudanças melhoraram a separação de responsabilidades, reduziram a
duplicação, tornaram os contratos de erro claros e facilitaram a manutenção e
validação do código sem alterar as funcionalidades previstas da aplicação.
