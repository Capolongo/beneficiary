package br.com.livelo.br.com.livelo.mapper;

import br.com.livelo.br.com.livelo.dto.UserDTO;
import br.com.livelo.br.com.livelo.entities.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private static final String USERNAME = "livelo";
    private static final String PASSWORD = "test";
    private static final String EMAIL = "livelo@test.com.br";

    @Test
    void shouldGenerateUserMappingMethod() {
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(getUser());
        assertThat(userDTO.getUsername()).isEqualTo(getUser().getUsername());
        assertThat(userDTO.getPassword()).isEqualTo(getUser().getPassword());
        assertThat(userDTO.getEmail()).isEqualTo(getUser().getEmail());

        User user = UserMapper.INSTANCE.userDTOtoUser(getUserDTO());
        assertThat(user.getUsername()).isEqualTo(getUserDTO().getUsername());
        assertThat(user.getPassword()).isEqualTo(getUserDTO().getPassword());
        assertThat(user.getEmail()).isEqualTo(getUserDTO().getEmail());
    }

    @Test
    void whenEntityIsNullShouldReturnNull() {
        assertNull(UserMapper.INSTANCE.userToUserDTO(null));
        assertNull(UserMapper.INSTANCE.userDTOtoUser(null));
    }

    private UserDTO getUserDTO() {
        return UserDTO.builder()
                .id(1)
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
