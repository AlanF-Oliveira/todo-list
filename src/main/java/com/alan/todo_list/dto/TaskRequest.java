package com.alan.todo_list.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class TaskRequest {
    @NotBlank
    private String title;
    private String description;
}
