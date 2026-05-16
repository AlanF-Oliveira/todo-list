package com.alan.todo_list.service;

import com.alan.todo_list.dto.TaskRequest;
import com.alan.todo_list.dto.TaskResponse;
import com.alan.todo_list.entity.Task;
import com.alan.todo_list.enums.TaskStatus;
import com.alan.todo_list.mapper.TaskMapper;
import com.alan.todo_list.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskMapper mapper;
    private final TaskRepository repository;

    public TaskResponse createTask(TaskRequest request) {
        Task task = mapper.toEntity(request);
        task.setStatus(TaskStatus.PENDING);
        Task savedTask  = repository.save(task);
        return mapper.toResponse(savedTask );
    }

    public List<TaskResponse> showAllTasks() {
        return mapper.toResponseList(repository.findAll());
    }

    public TaskResponse showTaskById(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada")));
    }

    public TaskResponse updateTask(Long id, TaskRequest request){
        Task task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        task.setDescription(request.getDescription());
        task.setTitle(request.getTitle());
        task.setStatus(request.getStatus());
        Task savedTask = repository.save(task);
        return mapper.toResponse(savedTask);
    }

    public void deleteTask(Long id){
        repository.deleteById(id);
    }

}
