package com.alan.todo_list.controller;

import com.alan.todo_list.dto.TaskRequest;
import com.alan.todo_list.dto.TaskResponse;
import com.alan.todo_list.dto.TaskUpdateRequest;
import com.alan.todo_list.enums.TaskStatus;
import com.alan.todo_list.exception.GlobalExceptionHandler;
import com.alan.todo_list.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @InjectMocks
    private TaskController controller;

    @Mock
    private TaskService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;
    private TaskRequest request;
    private TaskUpdateRequest updateRequest;
    private TaskResponse response;
    private String json;

    @BeforeEach
    void setup() throws JsonProcessingException {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .alwaysDo(print())
                .build();
        request = new TaskRequest("Título", "Descrição");
        updateRequest = new TaskUpdateRequest("Novo título", "Nova descrição", TaskStatus.COMPLETED);
        response = new TaskResponse(1L, "Título", "Descrição", TaskStatus.PENDING, null);
        json = objectMapper.writeValueAsString(request);
    }

    @Test
    void deveCriarTarefaComSucesso() throws Exception {
        when(service.createTask(request)).thenReturn(response);
        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Título"))
                .andExpect(jsonPath("$.status").value("PENDING"));
        verify(service).createTask(request);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deveListarTodasAsTarefas() throws Exception {
        when(service.showAllTasks()).thenReturn(List.of(response));
        mockMvc.perform(get("/task")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Título"));
        verify(service).showAllTasks();
        verifyNoMoreInteractions(service);
    }

    @Test
    void deveBuscarTarefaPorId() throws Exception {
        when(service.showTaskById(1L)).thenReturn(response);
        mockMvc.perform(get("/task/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Título"));
        verify(service).showTaskById(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deveRetornar404QuandoTarefaNaoEncontrada() throws Exception {
        when(service.showTaskById(99L))
                .thenThrow(new com.alan.todo_list.exception.ResourceNotFoundException("Tarefa não encontrada"));
        mockMvc.perform(get("/task/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service).showTaskById(99L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deveAtualizarTarefaComSucesso() throws Exception {
        TaskResponse updatedResponse = new TaskResponse(1L, "Novo título", "Nova descrição", TaskStatus.COMPLETED, null);
        when(service.updateTask(1L, updateRequest)).thenReturn(updatedResponse);
        mockMvc.perform(patch("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Novo título"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
        verify(service).updateTask(1L, updateRequest);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deveDeletarTarefaComSucesso() throws Exception {
        doNothing().when(service).deleteTaskById(1L);
        mockMvc.perform(delete("/task/1"))
                .andExpect(status().isNoContent());
        verify(service).deleteTaskById(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    void deveRetornar400QuandoTituloVazio() throws Exception {
        TaskRequest invalidRequest = new TaskRequest("", "Descrição");
        mockMvc.perform(post("/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }
}