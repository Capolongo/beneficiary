package br.com.livelo.br.com.livelo.services;

import br.com.livelo.br.com.livelo.dto.UserDTO;
import br.com.livelo.br.com.livelo.entities.User;
import br.com.livelo.br.com.livelo.exceptions.UserNotFoundException;
import br.com.livelo.br.com.livelo.mapper.UserMapperImpl;
import br.com.livelo.br.com.livelo.repositories.UserRepository;
import br.com.livelo.br.com.livelo.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final int USER_ID = 1;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "user@email.com";

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private UserMapperImpl userMapper;

    @BeforeEach
    void initService() {
        userService = new UserServiceImpl(userRepository, userMapper, null);
    }

    @Test
    void createUserShouldCreateAUser() {
        var user = getUser();
        when(userRepository.save(any(User.class))).thenReturn(user);
        var userSaved = userService.create(getUserDTO());

        assertThat(userSaved).usingRecursiveComparison().isEqualTo(user);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void retrieveUsersShouldReturnAllUsers() {
        var pagedMock = mock(Pageable.class);
        var user1 = getUser();
        var user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setEmail("user2@email.com");

        var users = List.of(user1, user2);
        when(userRepository.findAll(pagedMock)).thenReturn(new PageImpl<>(users));

        var result = userService.retrieveUsers(pagedMock);

        assertThat(users).isEqualTo(result.getContent());
        verify(userRepository).findAll(pagedMock);
    }

    @Test
    void findUserByIdShouldReturnAUser() {
        var user = new User();
        user.setId(1);
        user.setUsername("user2");
        user.setPassword("password2");
        user.setEmail("user2@email.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        var userFound = userService.findById(1);

        assertThat(userFound).isEqualTo(user);
        verify(userRepository).findById(1);
    }

    @Test
    void findByIdWithNonExistentIdShouldReturnUserNotFoundException() {
        assertThatThrownBy(() -> this.userService.findById(12345678))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User id [12345678] not found");
    }

    @Test
    void findUserByIdWithIdNullShouldReturnIllegalArgumentException() {
        assertThatThrownBy(() -> this.userService.findById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("userId is null");
    }

    @Test
    void createUserPassingNullShouldReturnIllegalArgumentException() {
        assertThatThrownBy(() -> this.userService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("user is null");
    }

    private UserDTO getUserDTO() {
        return UserDTO.builder()
                .id(USER_ID)
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .build();
    }

    private User getUser() {
        return User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .email(EMAIL)
                .build();
    }
}
