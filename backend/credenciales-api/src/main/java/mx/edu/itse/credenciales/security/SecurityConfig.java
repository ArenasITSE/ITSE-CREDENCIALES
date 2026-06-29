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

                        // ==========================================
                        // RUTAS PÚBLICAS
                        // ==========================================

                        .requestMatchers(
                                "/api/auth/**",
                                "/api/usuarios/**",
                                "/verificar/**",
                                "/api/credenciales/verificar/**",
                                "/uploads/**"
                        ).permitAll()

                        // ==========================================
                        // PERFIL
                        // ==========================================

                        .requestMatchers(
                                "/api/perfil/**"
                        ).hasAnyRole(
                                "ALUMNO",
                                "ADMINISTRADOR"
                        )

                        // ==========================================
                        // PORTAL DEL ALUMNO
                        // ==========================================

                        .requestMatchers(
                                "/api/alumno/**"
                        ).hasRole("ALUMNO")

                        // ==========================================
                        // PDF DE CREDENCIALES
                        // (Alumno también puede descargar)
                        // ==========================================

                        .requestMatchers(
                                "/api/credenciales/pdf/**"
                        ).permitAll()

                        // ==========================================
                        // ADMINISTRACIÓN DE ALUMNOS
                        // ==========================================

                        .requestMatchers(
                                "/api/alumnos/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                "/api/carreras/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                "/api/dashboard/**"
                        ).hasRole("ADMINISTRADOR")

                        // ==========================================
                        // CREDENCIALES
                        // SOLO ADMINISTRADORES
                        // ==========================================

                        .requestMatchers(
                                "/api/credenciales/**"
                        ).hasAnyRole(
                                "ADMINISTRADOR",
                                "CONTROL_ESCOLAR"
                        )

                        // ==========================================
                        // CUALQUIER OTRA RUTA
                        // ==========================================

                        .anyRequest()
                        .authenticated()

                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

}