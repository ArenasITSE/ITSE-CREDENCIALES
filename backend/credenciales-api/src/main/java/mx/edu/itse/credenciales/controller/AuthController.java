package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.LoginRequest;
import mx.edu.itse.credenciales.dto.LoginResponse;
import mx.edu.itse.credenciales.entity.Usuario;
import mx.edu.itse.credenciales.repository.UsuarioRepository;
import mx.edu.itse.credenciales.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {

        System.out.println("===== LOGIN =====");
        System.out.println("Username: " + request.getUsername());
        System.out.println("Password recibida: " + request.getPassword());

        Usuario usuario =
                usuarioRepository
                        .findByUsername(
                                request.getUsername()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Usuario no encontrado"
                                ));

        System.out.println("Password BD: " + usuario.getPassword());

        boolean coincide =
                passwordEncoder.matches(
                        request.getPassword(),
                        usuario.getPassword()
                );

        System.out.println("Coincide password: " + coincide);

        if (!coincide) {

            throw new RuntimeException(
                    "Contraseña incorrecta"
            );
        }

   String token =
        jwtService.generarToken(
                usuario.getUsername(),
                usuario.getRol().getNombre()
        );

        System.out.println("Token generado correctamente");

        return new LoginResponse(token);
    }
}