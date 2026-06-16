package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.PerfilResponse;
import mx.edu.itse.credenciales.service.PerfilService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import mx.edu.itse.credenciales.dto.CambiarPasswordDTO;


@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping
    public PerfilResponse perfil(
            Authentication authentication
    ) {

        return perfilService.obtenerPerfil(
                authentication.getName()
        );
    }

    @PutMapping("/cambiar-password")
    public String cambiarPassword(

            Authentication authentication,

            @RequestBody CambiarPasswordDTO dto

    ) {

        perfilService.cambiarPassword(

                authentication.getName(),

                dto

        );

        return "Contraseña actualizada correctamente";

    }

}