package com.mballem.demo_park_api.service;

import com.mballem.demo_park_api.entity.Usuario;
import com.mballem.demo_park_api.exception.PasswordInvalidException;
import com.mballem.demo_park_api.exception.UsernameUniqueViolationException;
import com.mballem.demo_park_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Spy
    private UsuarioService usuarioServiceSpy;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        usuario = criarUsuario();
    }

    private Usuario criarUsuario() {
        return new Usuario("valdivia@email.com", "123456", Usuario.Role.ROLE_CLIENTE);
    }

    @Test
    @DisplayName("Deveria salvar um usuário com sucesso")
    void salvarUsuarioComSucesso() {
        // Arrange (Configuração)
            // Use o ArgumentMatcher `any()` para interceptar qualquer instância do tipo Usuario
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act (Execução)
        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        // Assert (Validação)
        assertNotNull(usuarioSalvo);
        assertEquals("valdivia@email.com", usuarioSalvo.getUsername());
        assertEquals("123456", usuarioSalvo.getPassword());
        assertEquals(Usuario.Role.ROLE_CLIENTE, usuarioSalvo.getRole());

        // Verify (Verifique que o método save foi chamado uma vez)
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deveria lançar exceção quando o username já está cadastrado")
    void salvarUsuarioComUsernameDuplicado() {
        // Arrange
        when(usuarioRepository.save(any(Usuario.class))).thenThrow(DataIntegrityViolationException.class);

        // Act & Assert
        UsernameUniqueViolationException exception = assertThrows(UsernameUniqueViolationException.class, () -> {
            usuarioService.salvar(usuario);
        });

        assertEquals("Username {valdivia@email.com} ja cadastrado", exception.getMessage());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deveria alterar a senha com sucesso")
    void alterarSenhaComSucesso() {
        // Arrange
        Long userId = 1L;
        String senhaAtual = "senha123";
        String novaSenha = "novaSenha123";
        String confirmaSenha = "novaSenha123";

        // Crie o mock de um usuário com uma senha válida
        Usuario usuarioMock = new Usuario("valdivia@email.com", senhaAtual, Usuario.Role.ROLE_CLIENTE);

        // Mock para simular a busca do usuário por ID (usuarioRepository.findById(id))
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuarioMock));

        // Act
        Usuario usuarioAtualizado = usuarioService.editarSenha(userId, senhaAtual, novaSenha, confirmaSenha);

        // Assert
        assertNotNull(usuarioAtualizado);
        assertEquals(novaSenha, usuarioAtualizado.getPassword());

        // Verifica se o método findById foi chamado corretamente
        verify(usuarioRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Deveria lançar uma exceção quando a nova senha não confere com a confirmação")
    void novaSenhaNaoConfereComConfirmacao() {
        Long userId = 1L;
        String senhaAtual = "senha123";
        String novaSenha = "novaSenha123";
        String confirmaSenha = "123novaSenha";

        PasswordInvalidException exception = assertThrows(PasswordInvalidException.class, () -> {
            usuarioServiceSpy.editarSenha(userId, senhaAtual, novaSenha, confirmaSenha);
        });

        assertEquals("Nova senha não confere com confirmação de senha", exception.getMessage());

        // Certifica-se de que buscarPoId não foi chamado, pois o erro ocorreu antes
        verify(usuarioServiceSpy, never()).buscarPoId(anyLong());
    }

}