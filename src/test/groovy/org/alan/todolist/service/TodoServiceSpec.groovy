package org.alan.todolist.service

import org.alan.todolist.model.Alarm
import org.alan.todolist.model.Todo
import spock.lang.Specification
import org.alan.todolist.model.enums.Status

import java.time.Duration
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.NoSuchElementException


class TodoServiceSpec extends Specification {

    TodoService service;
    Todo todo;

    void setup() {

        service = new TodoService(new org.alan.todolist.repository.TodoRepository(), Clock.systemDefaultZone());
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

    def "deve ordenar os todos por prioridade durante o cadastro"() {

        given:
        Todo prioridadeBaixa = new Todo(
                "Prioridade baixa",
                "Descrição",
                LocalDateTime.now(),
                5,
                "Categoria",
                Status.TODO)
        Todo prioridadeAlta = new Todo(
                "Prioridade alta",
                "Descrição",
                LocalDateTime.now(),
                1,
                "Categoria",
                Status.TODO)

        when:
        service.addTodo(prioridadeBaixa)
        service.addTodo(prioridadeAlta)

        then:
        service.listAll() == [prioridadeAlta, prioridadeBaixa]
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

    def "deve retornar lista vazia quando nao encontrar a lista por categoria"() {

        given: "adiciona um objeto todo"
        service.addTodo(todo)
        String categoria = "Es223tudo"

        expect:
        service.listByCategory(categoria).isEmpty()
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

    def "deve retornar lista vazia quando nao encontrar a lista por prioridade"() {

        given: "adiciona um objeto todo"
        service.addTodo(todo)
        int prioridade = 1

        expect:
        service.listByPriority(prioridade).isEmpty()
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

    def "deve retornar lista vazia quando não encontrar a listagem por status"() {

        given: "adiciona um objeto todo"
        service.addTodo(todo)
        Status status = Status.DOING

        expect:
        service.listByStatus(status).isEmpty()
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
        service.deleteAllTodo()

        then: "lista vazia"
        service.listAll().isEmpty()
    }

    def "deve preservar o alarme durante a atualização"() {

        given:
        Alarm alarm = new Alarm(Duration.ofHours(1))
        todo.addAlarm(alarm)
        service.addTodo(todo)
        Todo novoTodo = new Todo(
                "Todo atualizado",
                "Descrição atualizada",
                LocalDateTime.now(),
                2,
                "Atualizada",
                Status.DOING)

        when:
        def resultado = service.updateTodo(todo.id, novoTodo)

        then:
        resultado.id == todo.id
        resultado.alarms == [alarm]
    }

    def "deve retornar uma cópia dos alarmes"() {

        given:
        Alarm alarm = new Alarm(Duration.ofHours(1))
        todo.addAlarm(alarm)

        when:
        def alarms = todo.alarms
        alarms.clear()

        then:
        todo.alarms == [alarm]
    }

    def "deve rejeitar repositório nulo"() {

        when:
        new TodoService(null, Clock.systemUTC())

        then:
        def exception = thrown(NullPointerException)
        exception.message == "Parâmetro inválido"
    }

    def "deve rejeitar relógio nulo"() {

        when:
        new TodoService(new org.alan.todolist.repository.TodoRepository(), null)

        then:
        def exception = thrown(NullPointerException)
        exception.message == "Parâmetro inválido"
    }

    def "deve rejeitar atualização com todo nulo"() {

        given:
        service.addTodo(todo)

        when:
        service.updateTodo(todo.id, null)

        then:
        def exception = thrown(NullPointerException)
        exception.message == "Parâmetro inválido"
    }

    def "deve lançar exceção ao buscar um id inexistente"() {

        when:
        service.findById(999999)

        then:
        def exception = thrown(NoSuchElementException)
        exception.message == "Todo não encontrado"
    }

    def "deve lançar exceção ao excluir um id inexistente"() {

        when:
        service.deleteTodoById(999999)

        then:
        def exception = thrown(NoSuchElementException)
        exception.message == "Todo não encontrado"
    }

    def "deve aceitar prioridade 1"() {

        when:
        def result = new Todo("Todo", "Descrição", LocalDateTime.now(), 1, "Categoria", Status.TODO)

        then:
        result.priority == 1
    }

    def "deve rejeitar prioridade inválida"() {

        when:
        new Todo("Todo", "Descrição", LocalDateTime.now(), prioridade, "Categoria", Status.TODO)

        then:
        def exception = thrown(IllegalArgumentException)
        exception.message == "Valor inválido."

        where:
        prioridade << [0, 6, null]
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

    def "deve avaliar alarmes usando o relógio injetado"() {

        given:
        Instant currentInstant = Instant.parse("2026-11-06T15:00:00Z")
        Clock fixedClock = Clock.fixed(currentInstant, ZoneOffset.UTC)
        TodoService fixedService = new TodoService(
                new org.alan.todolist.repository.TodoRepository(),
                fixedClock)
        Todo fixedTodo = new Todo(
                "Todo determinístico",
                "Descrição",
                LocalDateTime.of(2026, 11, 6, 16, 0),
                1,
                "Categoria",
                Status.TODO)
        fixedTodo.addAlarm(new Alarm(Duration.ofHours(1)))
        fixedService.addTodo(fixedTodo)

        expect:
        fixedService.checkPendingAlarms() == [fixedTodo]
    }

    def "não deve retornar alarme antes do momento de disparo"() {

        given:
        todo.finalDateTime = LocalDateTime.now().plusHours(2)
        todo.addAlarm(new Alarm(Duration.ofHours(1)))
        service.addTodo(todo)

        expect:
        service.checkPendingAlarms().isEmpty()
    }

    def "deve retornar alarme no momento de disparo"() {

        given:
        todo.finalDateTime = LocalDateTime.now().plusHours(1)
        todo.addAlarm(new Alarm(Duration.ofHours(1)))
        service.addTodo(todo)

        expect:
        service.checkPendingAlarms() == [todo]
    }

    def "não deve retornar alarme de tarefa concluída"() {

        given:
        todo.status = Status.DONE
        todo.addAlarm(new Alarm(Duration.ZERO))
        service.addTodo(todo)

        expect:
        service.checkPendingAlarms().isEmpty()
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
