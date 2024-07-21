package br.com.beneficiary.configure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserDetailsService userDetailsService;

    private MockMvc mockMvc;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testUserDetailsService() {
        UserDetails user = userDetailsService.loadUserByUsername("beneficiary");
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("beneficiary");
        assertThat(user.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");
    }

    @Test
    public void testPasswordEncoding() {
        String encodedPassword = passwordEncoder.encode("123");
        assertThat(passwordEncoder.matches("123", encodedPassword)).isTrue();
    }

    @Test
    public void testAccessWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/v1/beneficiary"))
                .andExpect(status().isUnauthorized());
    }
}
