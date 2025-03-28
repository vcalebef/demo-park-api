package com.mballem.demo_park_api.repository;

import com.mballem.demo_park_api.entity.Usuario;
import com.mballem.demo_park_api.web.dto.UsuarioCreateDto;
import com.mballem.demo_park_api.web.dto.mapper.UsuarioMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Deveria retornar o usuario com sucesso do banco de dados")
    void findUserByUsernameCase1() {
        String username = "victorcalebe@email.com";
        UsuarioCreateDto data = new UsuarioCreateDto(username, "123456");
        this.createUser(data);

        Usuario result = this.usuarioRepository.findUserByUsername(username);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);

    }

    @Test
    @DisplayName("Não deveria retornar o usuario quando tal não existir no banco de dados")
    void findUserByUsernameCase2() {
        String username = "victorcalebe@email.com";
        //UsuarioCreateDto data = new UsuarioCreateDto(username, "123456");
        //this.createUser(data);

        Usuario result = this.usuarioRepository.findUserByUsername(username);

        assertThat(result).isNull();

    }

    private Usuario createUser(UsuarioCreateDto data) {
        Usuario newUser = new Usuario(UsuarioMapper.toUsuario(data));
        this.entityManager.persist(newUser);
        this.entityManager.flush();
        return newUser;
    }
}