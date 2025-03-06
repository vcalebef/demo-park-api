package com.mballem.demo_park_api.service;

import com.mballem.demo_park_api.entity.Usuario;
import com.mballem.demo_park_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {

        if (!novaSenha.equals(confirmaSenha)) {
            throw new RuntimeException("Nova senha não confere com confirmação de senha.");
        }

        Usuario user = buscarPoId(id);
        if (!user.getPassword().equals(senhaAtual)) {
            throw new RuntimeException("Sua senha não confere");
        }

        user.setPassword(novaSenha);
        return user;

    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuarios() {

        return usuarioRepository.findAll();

    }
}
