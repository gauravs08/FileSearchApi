package com.gaurav.filesearchapiepassi.config;

import com.gaurav.filesearchapiepassi.handler.CustomAccessDeniedHandler;
import com.gaurav.filesearchapiepassi.Exception.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // User Creation
    @Bean
    public UserDetailsService users(AuthenticationManagerBuilder auth) throws Exception {

//        return auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password("{noop}password")
//                .authorities("ROLE_ADMIN_USER")
//                .and()
//                .withUser("user")
//                .password("{noop}password")
//                .authorities("ROLE_USER");

        UserDetails user = User.builder()
                .username("user")
                .password("{noop}password") // plain text password can be encrypted using different algorithms
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}password") // plain text password can be encrypted using different algorithms
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
                        requests ->
                                requests
                                        .requestMatchers(HttpMethod.POST, "/api/file/upload").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/api/users/welcome").hasAnyRole("ADMIN", "USER")
                                        .requestMatchers(HttpMethod.GET, "/api/up").hasAnyRole("ADMIN", "USER")
                                        .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint())
                        .accessDeniedHandler(accessDeniedHandler()));
        return http.build();
    }



    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
