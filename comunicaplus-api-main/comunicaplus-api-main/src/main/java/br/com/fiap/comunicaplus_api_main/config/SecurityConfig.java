package br.com.fiap.comunicaplus_api_main.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthFilter authFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                // Swagger e documentação
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()

                // H2 console
                .requestMatchers("/h2-console/**").permitAll()

                // Rotas de autenticação
                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/login/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/register/**").permitAll()

                // Usuários
                .requestMatchers(HttpMethod.POST, "/users", "/users/**").permitAll()
                .requestMatchers("/users/**").permitAll()  // <-- permite todos os métodos (GET, POST etc.)


                // Endpoints públicos específicos
                .requestMatchers(HttpMethod.POST, "/api/devices").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/devices/summary").permitAll() // Lista Dispositivos
                .requestMatchers(HttpMethod.GET, "/api/devices/**").permitAll() // Busca dispositivo por ID
                .requestMatchers(HttpMethod.DELETE, "/api/devices").permitAll() //Deleta dispositivo por ID

                // Rota de erro
                .requestMatchers("/error/**").permitAll()

                // CORS pré-flight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Demais endpoints exigem autenticação
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable()) // Para H2
                .httpStrictTransportSecurity(hsts -> hsts.disable()) // Dev
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfig() {
        var config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
