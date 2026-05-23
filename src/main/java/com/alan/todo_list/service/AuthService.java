package com.alan.api_autores_obras.service;

import com.alan.api_autores_obras.dto.auth.AuthResponse;
import com.alan.api_autores_obras.dto.auth.CadastroRequest;
import com.alan.api_autores_obras.dto.auth.LoginRequest;
import com.alan.api_autores_obras.entity.Usuario;
import com.alan.api_autores_obras.exception.ConflictException;
import com.alan.api_autores_obras.exception.ResourceNotFoundException;
import com.alan.api_autores_obras.exception.UnauthorizedException;
import com.alan.api_autores_obras.mapper.AuthMapper;
import com.alan.api_autores_obras.repository.UsuarioRepository;
import com.alan.api_autores_obras.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthMapper mapper;


    public AuthResponse cadastrarUsuario(CadastroRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email já cadastrado!");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Usuario entity = mapper.toEntity(request);
        usuarioRepository.save(entity);
        String token = jwtService.generateToken(entity);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Email ou senha inválidos");
        }
        String token = jwtService.generateToken(usuario);
        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
