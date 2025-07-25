package com.example.cash.config;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Unauthorized\"}");
                })
                )
                .formLogin(form -> form
                .loginProcessingUrl("/auth/login") // Frontend will POST here
                .successHandler((request, response, authentication) -> {
                    // No need to manually call rememberMeServices — Spring handles it
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Login success");
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Login failed");
                })
                )
                .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .deleteCookies("MY_SESSION_COOKIE", "remember-me")
                .logoutSuccessHandler((request, response, auth) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Logout success");
                })
                )
                .rememberMe(remember -> remember
                .key("SecretKey123")
                .rememberMeParameter("rememberMe")
                .tokenValiditySeconds(7 * 24 * 60 * 60)
                .userDetailsService(userDetailsService)
                .tokenRepository(persistentTokenRepository())
                );

        return http.build();
    }

    //this to allow cors for front end
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // tokenRepository.setCreateTableOnStartup(true); // Enable once to auto-create table
        return tokenRepository;
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices services
                = new PersistentTokenBasedRememberMeServices("SecretKey123", userDetailsService, persistentTokenRepository());
        services.setTokenValiditySeconds(7 * 24 * 60 * 60); // 7 days
        services.setParameter("rememberMe");
        return services;
    }

}
