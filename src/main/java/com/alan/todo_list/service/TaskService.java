package com.alan.todo_list.service;

import com.alan.todo_list.dto.TaskRequest;
import com.alan.todo_list.dto.TaskResponse;
import com.alan.todo_list.entity.Task;
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
        Task taskSaved = repository.save(task);
        return mapper.toResponse(taskSaved);
    }

    public List<TaskResponse> showAllTasks() {
        return mapper.toResponseList(repository.findAll());
    }

    public TaskResponse showTaskById(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada")));
    }



}
