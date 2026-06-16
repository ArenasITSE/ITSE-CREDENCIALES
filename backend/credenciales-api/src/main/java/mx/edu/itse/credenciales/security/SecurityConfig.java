package mx.edu.itse.credenciales.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtFilter jwtFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())
                                .cors(Customizer.withDefaults())

                                .authorizeHttpRequests(auth -> auth

                                                // Públicas
                                                .requestMatchers(
                                                                "/api/auth/**",
                                                                "/api/usuarios/**",
                                                                "/verificar/**",
                                                                "/api/credenciales/verificar/**",
                                                                "/uploads/**")
                                                .permitAll()

                                                // Perfil propio
                                                .requestMatchers(
                                                                "/api/perfil/**")
                                                .hasAnyRole(
                                                                "ALUMNO",
                                                                "ADMINISTRADOR")

                                                // Solo administradores
                                                .requestMatchers(
                                                                "/api/alumnos/**")
                                                .hasRole(
                                                                "ADMINISTRADOR")

                                                .requestMatchers(
                                                                "/api/carreras/**")
                                                .hasRole(
                                                                "ADMINISTRADOR")

                                                .requestMatchers(
                                                                "/api/dashboard/**")
                                                .hasRole("ADMINISTRADOR")

                                                // Administrador y Control Escolar
                                                .requestMatchers(
                                                                "/api/credenciales/**")
                                                .hasAnyRole(
                                                                "ADMINISTRADOR",
                                                                "CONTROL_ESCOLAR")

                                                .anyRequest()
                                                .authenticated())

                                .addFilterBefore(
                                                jwtFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {

                return new BCryptPasswordEncoder();

        }
}