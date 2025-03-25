package com.mballem.demo_park_api;

import com.mballem.demo_park_api.web.dto.UsuarioCreateDto;
import com.mballem.demo_park_api.web.dto.UsuarioResponseDto;
import com.mballem.demo_park_api.web.dto.UsuarioSenhaDto;
import com.mballem.demo_park_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createUsuario_ComUsernameEPasswordValidos_RetornoUsuarioCriadoStatus201() {
        UsuarioResponseDto usuarioResponseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("vitorroque@email.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getUsername()).isEqualTo("vitorroque@email.com");
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getRole()).isEqualTo("CLIENTE");

    }


    @Test
    public void createUsuario_ComUsernameInvalido_RetornarErrorMessageStatus422() {
        ErrorMessage usuarioResponseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(422);

        usuarioResponseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("vict@", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(422);

        usuarioResponseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("victo@email.", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUsuario_ComPasswordInvalido_RetornarErrorMessageStatus422() {
        ErrorMessage usuarioResponseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("vitorroque@email.com", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(422);

        usuarioResponseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("vitorroque@email.com", "12346"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(422);

        usuarioResponseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("vitorroque@email.com", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUsuario_ComUsernameRepetido_RetornoErrorMessage409() {
        ErrorMessage usuarioResponseDto = webTestClient
                .post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDto("victor@email.com", "123456"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(409);

    }

    @Test
    public void buscarUsuario_ComIdExistente_RetornoUsuarioComStatus200() {
        UsuarioResponseDto usuarioResponseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UsuarioResponseDto.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getId()).isEqualTo(100);
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getUsername()).isEqualTo("victor@email.com");
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getRole()).isEqualTo("ADMIN");

    }

    @Test
    public void buscarUsuario_ComIdInexistente_RetornarErrorMessageComStatus404() {
        ErrorMessage usuarioResponseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios/122")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(404);

    }

    @Test
    public void editarSenha_ComDadosValidos_RetornarStatus204() {
    webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void editarSenhaComIdInexistente_RetornarErrorMessageComStatus404() {
        ErrorMessage usuarioResponseDto = webTestClient
                .patch()
                .uri("/api/v1/usuarios/989")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(404);
    }

    @Test
    public void editarSenhaComCamposInvalidos_RetornarErrorMessageComStatus422() {
        ErrorMessage usuarioResponseDto = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("", "", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(422);

        usuarioResponseDto = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("1234", "1234", "1234"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(422);

        usuarioResponseDto = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("12345678", "12345678", "12345678"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(422);
    }

    @Test
    public void editarSenhaComCamposQueNaoConferem_RetornarErrorMessageComStatus400() {
        ErrorMessage usuarioResponseDto = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("123456", "123456", "000000"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(400);

        usuarioResponseDto = webTestClient
                .patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDto("000000", "123456", "123456"))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto.getStatus()).isEqualTo(400);

    }

    @Test
    public void buscarUsuarios_RetornarStatus200() {
        List<UsuarioResponseDto> usuarioResponseDto = webTestClient
                .get()
                .uri("/api/v1/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioResponseDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(usuarioResponseDto).isNotNull();

    }
}
