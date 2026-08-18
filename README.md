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

## Alarme de tarefas

Tarefas podem ter alarmes configurados, avisando uma ou mais horas antes do prazo. A cada retorno ao menu, alarmes pendentes são exibidos. Tarefas `DONE` não disparam alarme.
