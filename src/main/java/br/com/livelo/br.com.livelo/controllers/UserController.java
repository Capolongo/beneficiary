package br.com.livelo.br.com.livelo.controllers;

import br.com.livelo.br.com.livelo.dto.UserDTO;
import br.com.livelo.br.com.livelo.entities.User;
import br.com.livelo.br.com.livelo.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public URI createUser(@RequestBody @Valid UserDTO user) {
        log.info("Create user >>> {}", user);
        var savedUser = userService.create(user);
        log.info("User saved successfully {}", savedUser);

        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();
    }

    @GetMapping
    public Page<User> retrieveUsers(Pageable pageable) {
        return userService.retrieveUsers(pageable);
    }

    @GetMapping(value = "/{id}")
    public User retrieveUser(@PathVariable("id") int id) {
        return userService.findById(id);
    }

}
