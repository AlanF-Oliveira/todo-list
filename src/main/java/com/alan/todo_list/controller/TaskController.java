package com.alan.todo_list.controller;

import com.alan.todo_list.dto.TaskRequest;
import com.alan.todo_list.dto.TaskResponse;
import com.alan.todo_list.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createTask(request));
    }

    @GetMapping
    public ResponseEntity <List<TaskResponse>> showAllTasks(){
        return ResponseEntity.status(HttpStatus.OK).body(service.showAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity <TaskResponse> showTaskById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.showTaskById(id));
    }

}
