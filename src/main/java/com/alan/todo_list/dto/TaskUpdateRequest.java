package com.alan.todo_list.dto;

import com.alan.todo_list.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class TaskUpdateRequest {
    private String title;
    private String description;
    @Schema(description = "Status da tarefa", allowableValues = {"PENDING", "COMPLETED", "CANCELED"})
    private TaskStatus status;
}
