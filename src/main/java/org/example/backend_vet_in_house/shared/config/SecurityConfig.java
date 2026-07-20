package org.example.backend_vet_in_house.shared.config;

import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.shared.config.filter.JwtFilterValidator;
import org.example.backend_vet_in_house.users.utils.JwtUtil;
import org.example.backend_vet_in_house.users.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(http -> {

                    http.requestMatchers("/api/v1/auth/**").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/register");
                    http.requestMatchers(HttpMethod.POST, "/login");

                    http.requestMatchers("/api/v1/product/**").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/create").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/find-all").permitAll();

                    http.requestMatchers("/api/v1/orders/**").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/create").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/find-all").permitAll();

                    http.requestMatchers("/api/v1/user/**").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/find/order-history").permitAll();

                    http.requestMatchers("/error").permitAll();
                    http.anyRequest().denyAll();
                })
                .addFilterBefore(new JwtFilterValidator(jwtUtil), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
