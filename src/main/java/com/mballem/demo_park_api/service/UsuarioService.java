package com.mballem.demo_park_api.service;

import com.mballem.demo_park_api.entity.Usuario;
import com.mballem.demo_park_api.exception.EntityNotFoundException;
import com.mballem.demo_park_api.exception.PasswordInvalidException;
import com.mballem.demo_park_api.exception.UsernameUniqueViolationException;
import com.mballem.demo_park_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {

        try {
            return usuarioRepository.save(usuario);
        }catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username {%s} ja cadastrado", usuario.getUsername()));
        }


    }

    @Transactional(readOnly = true)
    public Usuario buscarPoId(Long id) {

        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuario cujo id é {%s} nao encontrado", id))
        );

    }

    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {

        if (!novaSenha.equals(confirmaSenha)) {
            throw new PasswordInvalidException(String.format("Nova senha não confere com confirmação de senha"));
        }

        Usuario user = buscarPoId(id);
        if (!user.getPassword().equals(senhaAtual)) {
            throw new PasswordInvalidException(String.format("Sua senha não confere"));
        }

        user.setPassword(novaSenha);
        return user;

    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuarios() {

        return usuarioRepository.findAll();

    }

    @Transactional(readOnly = true)
    public Usuario findUserByUsername(String username) {

        return usuarioRepository.findUserByUsername(username);

    }
}
