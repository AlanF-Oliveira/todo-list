package com.alan.todo_list.controller;


import com.alan.todo_list.dto.auth.AuthResponse;
import com.alan.todo_list.dto.auth.CadastroRequest;
import com.alan.todo_list.dto.auth.LoginRequest;
import com.alan.todo_list.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/cadastrar")
    public ResponseEntity<AuthResponse> cadastrarUsuario(@RequestBody @Valid CadastroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.cadastrarUsuario(request));
    }

    @PostMapping("/entrar")
    public ResponseEntity<AuthResponse> login (@RequestBody @Valid LoginRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
    }

}
