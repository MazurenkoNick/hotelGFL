package com.example.hotelgfl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable) // todo: handle csrf
                .securityContext(customizer -> customizer.requireExplicitSave(false))
                .authorizeHttpRequests(customizer ->
                        customizer
                                .requestMatchers("/form-login", "/login", "/css/**").permitAll()
                                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(login -> login
                        .loginPage("/form-login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true) // Redirect after successful login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/form-login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID") // todo: reconfigure for jwt token
                );

        return http.build();
    }
}
