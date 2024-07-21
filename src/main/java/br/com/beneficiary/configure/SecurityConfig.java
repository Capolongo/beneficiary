package br.com.beneficiary.configure;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${security.user.name}")
    private String userName;

    @Value("${security.user.password}")
    private String password;

    @Value("${security.user.roles}")
    private String roles;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/h2-console/**").permitAll() // Permite acesso ao H2 Console
                                .anyRequest().authenticated() // Requer autenticação para todas as outras requisições
                )
                .httpBasic(httpBasic ->
                        httpBasic
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.getWriter().write("Not authorized, please include username and password");
                                })
                )
                .csrf(csrf ->
                        csrf
                                .ignoringRequestMatchers("/h2-console/**") // Ignora CSRF para o H2 Console
                                .ignoringRequestMatchers("/v1/**") // Ajuste o caminho para suas APIs
                )
                .headers(headers ->
                        headers
                                .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Configura X-Frame-Options
                                .contentSecurityPolicy(csp ->
                                        csp.policyDirectives("frame-ancestors 'self'") // Configura Content-Security-Policy
                                )
                );

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername(userName)
                .password(passwordEncoder().encode(password))
                .roles(roles)
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
