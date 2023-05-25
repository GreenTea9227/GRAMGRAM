package com.ll.gramgram.base.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import static com.ll.gramgram.base.security.Role.ADMIN;
import static com.ll.gramgram.base.security.Role.INSTAGRAM;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2AccessTokenResponseClient oAuth2AccessTokenResponseClient;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/usr/member/login")
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/usr/member/login")
                                .tokenEndpoint(t -> t
                                        .accessTokenResponseClient(oAuth2AccessTokenResponseClient)
                                )
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/usr/member/logout")
                                .invalidateHttpSession(true)
                );
        http
                .authorizeHttpRequests()
                .requestMatchers("/error", "/resource/common/**", "/","/common/**",
                        "/usr/home/about", "/actuator/**",
                        "/favicon.ico").permitAll()
                .requestMatchers("/usr/member/login","/usr/member/findPassword").anonymous()
                .requestMatchers("/usr/likeablePerson/**").hasAnyRole(INSTAGRAM.name(), ADMIN.name())
                .requestMatchers("/usr/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
