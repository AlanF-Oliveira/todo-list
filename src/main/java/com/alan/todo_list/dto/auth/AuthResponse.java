package com.alan.todo_list.dto.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuthResponse {
    private String token;
}
