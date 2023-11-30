package br.com.livelo.br.com.livelo.services.impl;


import br.com.livelo.br.com.livelo.dto.UserDTO;
import br.com.livelo.br.com.livelo.entities.User;
import br.com.livelo.br.com.livelo.exceptions.UserNotFoundException;
import br.com.livelo.br.com.livelo.mapper.UserMapper;
import br.com.livelo.br.com.livelo.repositories.UserRepository;
import br.com.livelo.br.com.livelo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, @Autowired UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Page<User> retrieveUsers(Pageable pageable) {
        log.info("Retrieve users");
        var users = this.userRepository.findAll(pageable);
        log.info("Retrieved {} users", users.getTotalElements());

        return users;
    }

    @Override
    public UserDTO create(UserDTO user) {
        if (Objects.isNull(user)) {
            log.error("user is null");
            throw new IllegalArgumentException("user is null");
        }
        log.info("Creating user {}", user);
        user.setId(null); // dismiss id case informed on create
        log.info("Saving user");
        var entity = userMapper.userDTOtoUser(user);
        var userSaved = this.userRepository.save(entity);
        log.info("User saved {}", user);



        return userMapper.userToUserDTO(userSaved);
    }

    @Override
    public User findById(Integer userId) {
        if (Objects.isNull(userId)) {
            log.error("userId can not be null");
            throw new IllegalArgumentException("userId is null");
        }
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("User id {}  not found", userId);
            throw new UserNotFoundException("User id [" + userId + "] not found");
        }
        log.info("User found {} ", user.get());
        return user.get();
    }

}
