package com.alan.todo_list.dto;

import com.alan.todo_list.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskRequest {
    @NotBlank
    private String title;
    private String description;
}
