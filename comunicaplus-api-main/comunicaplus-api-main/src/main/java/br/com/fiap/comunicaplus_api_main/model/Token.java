package br.com.fiap.comunicaplus_api_main.model;

public record Token(
    String token,
    Long expiration,
    String type
) {}
