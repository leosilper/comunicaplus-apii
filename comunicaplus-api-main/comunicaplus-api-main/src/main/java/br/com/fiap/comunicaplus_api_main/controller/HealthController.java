package br.com.fiap.comunicaplus_api_main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String healthCheck() {
        return "ComunicaPlus API está rodando!";
    }
}
