# TODO List 

**Autor:** Alan Ferreira Oliveira

Aplicação backend de lista de tarefas, feita em Java.

## Tecnologias

- Java 8
- Gradle 
## Como executar

```bash
git clone https://github.com/AlanF-Oliveira/todo-list.git
cd todo-list
./gradlew run
```

Ou rode a classe `org.alan.todolist.TodoList` diretamente pela IDE.

## Sobre a solução

O projeto é organizado em camadas (`model`, `repository`, `service`, `terminal`). Tarefas têm nome, descrição, data de término, prioridade (1-5), categoria e status (TODO/DOING/DONE). Ao cadastrar uma nova tarefa, ela é inserida automaticamente na posição correta da lista, mantendo a ordenação por prioridade.

