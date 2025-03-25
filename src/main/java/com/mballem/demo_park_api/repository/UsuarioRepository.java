package com.mballem.demo_park_api.repository;

import com.mballem.demo_park_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findUserByUsername(String username);

}
