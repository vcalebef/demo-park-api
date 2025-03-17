package com.mballem.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioSenhaDto {

    @Size(min = 6, max = 6)
    @NotBlank
    private String senhaAtual;
    @Size(min = 6, max = 6)
    @NotBlank
    private String novaSenha;
    @Size(min = 6, max = 6)
    @NotBlank
    private String confirmaSenha;

}
