package com.alan.todo_list.service;

import com.alan.todo_list.dto.TaskRequest;
import com.alan.todo_list.dto.TaskResponse;
import com.alan.todo_list.dto.TaskUpdateRequest;
import com.alan.todo_list.entity.Task;
import com.alan.todo_list.entity.Usuario;
import com.alan.todo_list.enums.TaskStatus;
import com.alan.todo_list.exception.ResourceNotFoundException;
import com.alan.todo_list.mapper.TaskMapper;
import com.alan.todo_list.repository.TaskRepository;
import com.alan.todo_list.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskMapper mapper;
    private final TaskRepository repository;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public TaskResponse createTask(TaskRequest request) {
        Task task = mapper.toEntity(request);
        task.setStatus(TaskStatus.PENDING);
        task.setUsuario(getUsuarioLogado());
        Task savedTask = repository.save(task);
        return mapper.toResponse(savedTask);
    }

    public List<TaskResponse> showAllTasks() {
        return mapper.toResponseList(repository.findAllByUsuario(getUsuarioLogado()));
    }

    public TaskResponse showTaskById(Long id) {
        return mapper.toResponse(repository.findByIdAndUsuario(id, getUsuarioLogado())
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada")));
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task task = repository.findByIdAndUsuario(id, getUsuarioLogado())
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));
        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        Task savedTask = repository.save(task);
        return mapper.toResponse(savedTask);
    }

    public void deleteTaskById(Long id) {
        Task task = repository.findByIdAndUsuario(id, getUsuarioLogado()) // filtra
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada"));
        repository.delete(task);
    }

}
