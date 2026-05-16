package com.alan.todo_list.service;

import com.alan.todo_list.dto.TaskRequest;
import com.alan.todo_list.dto.TaskResponse;
import com.alan.todo_list.dto.TaskUpdateRequest;
import com.alan.todo_list.entity.Task;
import com.alan.todo_list.enums.TaskStatus;
import com.alan.todo_list.exception.ResourceNotFoundException;
import com.alan.todo_list.mapper.TaskMapper;
import com.alan.todo_list.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskService service;

    private Task task;
    private TaskRequest request;
    private TaskUpdateRequest updateRequest;
    private TaskResponse response;

    @BeforeEach
    void setup() {
        request = new TaskRequest("Título", "Descrição");
        task = Task.builder()
                .title("Título")
                .description("Descrição")
                .status(TaskStatus.PENDING)
                .build();
        updateRequest = new TaskUpdateRequest("Novo título", "Nova descrição", TaskStatus.COMPLETED);
        response = new TaskResponse(1L, "Título", "Descrição", TaskStatus.PENDING, null);
    }

    @Test
    void deveCriarTarefaComSucesso() {
        when(mapper.toEntity(request)).thenReturn(task);
        when(repository.save(task)).thenReturn(task);
        when(mapper.toResponse(task)).thenReturn(response);
        TaskResponse result = service.createTask(request);
        assertEquals("Título", result.getTitle());
        assertEquals(TaskStatus.PENDING, result.getStatus());
        verify(repository).save(task);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void deveListarTodasAsTarefas() {
        when(repository.findAll()).thenReturn(List.of(task));
        when(mapper.toResponseList(List.of(task))).thenReturn(List.of(response));
        List<TaskResponse> result = service.showAllTasks();
        assertEquals(1, result.size());
        verify(repository).findAll();
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void deveBuscarTarefaPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(mapper.toResponse(task)).thenReturn(response);
        TaskResponse result = service.showTaskById(1L);
        assertEquals("Título", result.getTitle());
        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.showTaskById(99L));
        verify(repository).findById(99L);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void deveAtualizarTarefaComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(task));
        when(repository.save(task)).thenReturn(task);
        when(mapper.toResponse(task)).thenReturn(response);
        service.updateTask(1L, updateRequest);
        assertEquals("Novo título", task.getTitle());
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
        verify(repository).findById(1L);
        verify(repository).save(task);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void deveDeletarTarefaComSucesso() {
        doNothing().when(repository).deleteById(1L);
        service.deleteTaskById(1L);
        verify(repository).deleteById(1L);
        verifyNoMoreInteractions(repository, mapper);
    }
}