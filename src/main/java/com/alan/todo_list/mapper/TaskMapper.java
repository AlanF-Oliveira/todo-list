package com.alan.todo_list.mapper;

import com.alan.todo_list.dto.TaskRequest;
import com.alan.todo_list.dto.TaskResponse;
import com.alan.todo_list.entity.Task;
import com.alan.todo_list.enums.TaskStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {
    public Task toEntity(TaskRequest request){
        return Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TaskStatus.PENDING)
                .build();
    }

    public TaskResponse toResponse(Task task){
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .build();

    }

    public List<TaskResponse> toResponseList(List<Task> tasks) {
        return tasks.stream().map(this:: toResponse).toList();
    }
}
