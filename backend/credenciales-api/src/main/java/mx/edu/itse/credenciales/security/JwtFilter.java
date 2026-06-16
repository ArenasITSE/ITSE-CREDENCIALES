package mx.edu.itse.credenciales.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("================================");
        System.out.println("URL: " + request.getRequestURI());

        String authHeader =
                request.getHeader("Authorization");

        System.out.println("Authorization: " + authHeader);

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            System.out.println("Sin token, continuando...");

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        String token =
                authHeader.substring(7);

        System.out.println("Token recibido: " + token);

        if (jwtService.validarToken(token)) {

            System.out.println("Token válido");

            String username =
                    jwtService.extraerUsername(token);

                    String rol =
        jwtService.extraerRol(token);

System.out.println(
        "Rol token: " + rol
);

            System.out.println("Usuario token: " + username);

           UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + rol
                        )
                )
        );

            auth.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(auth);
                    System.out.println(
        "Authorities: " +
        auth.getAuthorities()
);

        } else {

            System.out.println("Token inválido");

        }

        filterChain.doFilter(
                request,
                response
        );
    }
}