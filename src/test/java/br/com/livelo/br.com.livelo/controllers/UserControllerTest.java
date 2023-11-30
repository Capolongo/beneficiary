package br.com.livelo.br.com.livelo.controllers;

import br.com.livelo.br.com.livelo.dto.UserDTO;
import br.com.livelo.br.com.livelo.entities.User;
import br.com.livelo.br.com.livelo.services.UserService;
import br.com.livelo.br.com.livelo.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
class UserControllerTest {

    private static final String BASE_PATH = "/v1/users";
    private static final String USERNAME = "livelo";
    private static final String PASSWORD = "test";
    private static final String EMAIL = "livelo@test.com.br";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void createUserShouldReturnStatusCreated() throws Exception {
        var user = getUserDTO();
        when(userService.create(any(UserDTO.class))).thenReturn(user);

        var inputInJson = JsonUtils.convert(user);
        var requestBuilder = MockMvcRequestBuilders
                .post(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputInJson)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
        verify(userService).create(any(UserDTO.class));
    }

    @Test
    void retrieveUserShouldRetrieveASavedUser() throws Exception {
        var user = getUser();
        when(userService.findById(1)).thenReturn(user);
        var URI = BASE_PATH + "/1";
        var requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .accept(MediaType.APPLICATION_JSON);

        var result = mockMvc.perform(requestBuilder).andReturn();

        var expectedJson = JsonUtils.convert(user);
        var outputInJson = result.getResponse().getContentAsString();

        assertThat(outputInJson).isEqualTo(expectedJson);
        verify(userService).findById(1);
    }

    @Test
    void retrieveUsersShouldReturnAPageOfUsers() throws Exception {
        var user = getUser();
        var user2 = User.builder()
                .username("user2")
                .password(PASSWORD)
                .email("user2@email.com")
                .build();

        var pagedUser = new PageImpl<>(Arrays.asList(user, user2));
        when(userService.retrieveUsers(any(Pageable.class))).thenReturn(pagedUser);
        var requestBuilder = MockMvcRequestBuilders
                .get(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON);
        var mockResult = mockMvc.perform(requestBuilder).andReturn();

        var expectedJson = JsonUtils.convert(pagedUser);
        var outputJson = mockResult.getResponse().getContentAsString();
        assertThat(outputJson).isEqualTo(expectedJson);
        verify(userService).retrieveUsers(any(Pageable.class));
    }

    @Test
    void createUsertPropertiesNullShouldReturnBadRequest() throws Exception {
        var inputInJson = JsonUtils.convert(new User());
        var requestBuilder = MockMvcRequestBuilders
                .post(BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputInJson)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
        verifyNoInteractions(userService);
    }

    @Test
    void findByIdShouldReturnServerError() throws Exception {
        when(userService.findById(any(Integer.class))).thenThrow(new RuntimeException("Unexpected error!"));

        var URI = BASE_PATH + "/2";
        var requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isInternalServerError());
    }

    private UserDTO getUserDTO() {
        return UserDTO.builder()
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
