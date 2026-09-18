package org.alan.todolist.service

import org.alan.todolist.model.Alarm
import org.alan.todolist.model.Todo
import spock.lang.Specification
import org.alan.todolist.model.enums.Status

import java.time.Duration
import java.time.LocalDateTime


class TodoServiceSpec extends Specification {

    TodoService service;
    Todo todo;

    void setup() {

        service = new TodoService();
        todo = new Todo(
                "Estudar Spock",
                "Finalizar os testes unitários do projeto",
                LocalDateTime.now(),
                3,
                "Estudo",
                Status.TODO)
    }

    def "deve cadastrar um Todo"() {

        expect: "a lista começa vazia"
        service.listAll().isEmpty()

        when: "chama o cadastro de todo da service"
        def resultado = service.addTodo(todo)

        then: "checa os dados cadastrados"
        service.listAll().size() == 1
        resultado.name == todo.name
        resultado.is(todo)
    }

    def "deve listar os TODOS cadastrados"() {

        given: "um todo cadastrado"
        service.addTodo(todo)

        when: "executa a listagem pde todos os elementos da lista"
        def resultado = service.listAll()

        then: "checa se os dados batem"
        resultado.size() == 1
        resultado[0].name == todo.name
    }

    def "deve lista por categoria"() {

        given: "adiciona um objeto todo"
        service.addTodo(todo)
        String categoria = "Estudo"

        when: "executa a listagem por categoria"
        def resultado = service.listByCategory(categoria)

        then: "checa se os dados batem"
        resultado.size() == 1
        resultado[0].category == todo.category
    }

    def "deve chamar uma exception quando nao encontrar a lista por categoria"() {

        given: "adiciona um objeto todo"
        service.addTodo(todo)
        String categoria = "Es223tudo"

        when: "executa a listagem por categoria"
        service.listByCategory(categoria)


        then: "checa se a exception foi chamada"
        //thrown(ArrayIndexOutOfBoundsException)
        Exception e = thrown(ArrayIndexOutOfBoundsException)
        e.message == "Categoria não encontrada"
    }

    def "deve listar por prioridade"() {

        given: "adiciona um objeto todo"
        service.addTodo(todo)
        int prioridade = 3

        when: "executa a listagem por prioridade"
        def resultado = service.listByPriority(prioridade)

        then: "checa se os dados batem"
        resultado.size() == 1
        resultado[0].priority == todo.priority
    }

    def "deve chamar uma exception quando nao encontrar a lista por prioridade"() {

        given: "adiciona um objeto todo"
        service.addTodo(todo)
        int prioridade = 1

        when: "executa a listagem por categoria"
        service.listByPriority(prioridade)


        then: "checa se a exception foi chamada"
        //thrown(ArrayIndexOutOfBoundsException)
        Exception e = thrown(ArrayIndexOutOfBoundsException)
        e.message == "Prioridade não encontrada"
    }

    def "deve listar por status"() {

        given: "adiciona um objeto todo"
        service.addTodo(todo)
        Status status = Status.TODO

        when: "executa a listagem por status"
        def resultado = service.listByStatus(status)

        then: "checa se os dados batem"
        resultado.size() == 1
        resultado[0].status == todo.status

    }

    def "deve chamar uma exception quando não encontrar a listagem por status"() {

        given: "adiciona um objeto todo"
        service.addTodo(todo)
        Status status = Status.DOING

        when: "executa a listagem por status"
        service.listByStatus(status)

        then: "checa se a exception foi chamada"
        Exception e = thrown()
        e.message == "Status não encontrado"
    }

    def "deleta um todo por id"() {

        given: "um todo cadastrado"
        service.addTodo(todo)

        when: "executa o delete por id"
        service.deleteTodoById(todo.id)

        then: "lista vazia"
        service.listAll().isEmpty()
    }

    def "deve deletar toda a lista"() {

        given: "cadastra 2 todos"
        service.addTodo(todo)
        service.addTodo(new Todo("teste", "teste", LocalDateTime.now(), 1, "teste", Status.DOING))

        when: "executa o delete all"
        service.delelteAllTodo()

        then: "lista vazia"
        service.listAll().isEmpty()
    }

    def "deve retornar todo com alarme vencido"() {

        given: "alarme de 1h configurado"
        todo.addAlarm(new Alarm(Duration.ofHours(1)))
        service.addTodo(todo)

        when:
        def resultado = service.checkPendingAlarms()

        then:
        resultado.size() == 1
        resultado[0].name == todo.name
    }

    def "deve alterar um todo"(){

        given: "cadastra um todo e cria um novo todo para teste"
        service.addTodo(todo)
        Todo novoTodo = new Todo(
                "Todo atualizado",
                "Novo todo",
                LocalDateTime.now(),
                1,
                "TESTE",
                Status.TODO
        )

        when: "executa o update "
        def resultado = service.updateTodo(todo.id, novoTodo)

        then: "compara se o update foi feito com sucesso"
        resultado.name == novoTodo.name
        resultado.category == novoTodo.category
        resultado.id == todo.id
    }
}
