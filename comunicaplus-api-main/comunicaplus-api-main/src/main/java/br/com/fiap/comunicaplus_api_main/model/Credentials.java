package br.com.fiap.comunicaplus_api_main.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Credentials(
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    String password
) {}
