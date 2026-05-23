package com.alan.todo_list.repository;

import com.alan.todo_list.entity.Task;
import com.alan.todo_list.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUsuario(Usuario usuario);
    Optional<Task> findByIdAndUsuario(Long id, Usuario usuario);
}
