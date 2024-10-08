package com.example.shop.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Config class to allow only predefined users to make requests to this service.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Save the in memory hardcoded users.
   */
  @Bean
  public UserDetailsService users() {
    UserDetails admin = User
        .withUsername("admin")
        .password(bCryptPasswordEncoder().encode("admin"))
        .roles("ADMIN", "USER")
        .build();

    UserDetails user = User
        .withUsername("test_user")
        .password(bCryptPasswordEncoder().encode("test_password"))
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(admin, user);
  }

  /**
   * Allow only ADMIN users to modify the contents of the Shop.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST).hasRole("ADMIN")
            .requestMatchers(HttpMethod.PATCH).hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE).hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET).hasRole("USER")
            .anyRequest().authenticated()
        )
        .httpBasic(withDefaults())
        .csrf(AbstractHttpConfigurer::disable);

    return httpSecurity.build();
  }
}
