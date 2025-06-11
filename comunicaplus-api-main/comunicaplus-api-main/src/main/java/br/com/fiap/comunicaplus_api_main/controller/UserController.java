package br.com.fiap.comunicaplus_api_main.controller;

import br.com.fiap.comunicaplus_api_main.model.User;
import br.com.fiap.comunicaplus_api_main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = repository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = repository.findAll();
        return ResponseEntity.ok(users);
    }
}
