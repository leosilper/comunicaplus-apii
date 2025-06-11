package br.com.fiap.comunicaplus_api_main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.comunicaplus_api_main.repository.UserRepository;

@RestController
@RequestMapping("/api/device-users")
public class DeviceUserController {

    @Autowired
    private UserRepository repository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
