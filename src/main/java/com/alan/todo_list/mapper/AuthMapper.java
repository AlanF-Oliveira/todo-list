package com.alan.todo_list.mapper;


import com.alan.todo_list.dto.auth.CadastroRequest;
import com.alan.todo_list.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
   public Usuario toEntity(CadastroRequest request){
       return Usuario.builder()
               .nome(request.getNome())
               .email(request.getEmail())
               .password(request.getPassword())
               .build();
   }

}
