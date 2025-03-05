package com.mballem.demo_park_api.service;

import com.mballem.demo_park_api.entity.Usuario;
import com.mballem.demo_park_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {

        return usuarioRepository.save(usuario);

    }

    @Transactional(readOnly = true)
    public Usuario buscarPoId(Long id) {

        return usuarioRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Usuario nao encontrado")
        );

    }

    @Transactional
    public Usuario editarSenha(Long id, String password) {

        Usuario user = buscarPoId(id);
        user.setPassword(password);
        return user;

    }
}
