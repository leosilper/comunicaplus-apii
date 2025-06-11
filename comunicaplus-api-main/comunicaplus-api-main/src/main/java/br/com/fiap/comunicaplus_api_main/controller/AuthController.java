package br.com.fiap.comunicaplus_api_main.controller;

import br.com.fiap.comunicaplus_api_main.model.Credentials;
import br.com.fiap.comunicaplus_api_main.model.Token;
import br.com.fiap.comunicaplus_api_main.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Token login(@RequestBody @Valid Credentials credentials) {
        log.info("Attempting login with: {}", credentials);
        return authService.login(credentials);
    }
}
