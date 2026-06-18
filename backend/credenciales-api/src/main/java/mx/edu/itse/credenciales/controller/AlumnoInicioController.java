package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.AlumnoInicioDTO;
import mx.edu.itse.credenciales.service.AlumnoInicioService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alumno")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlumnoInicioController {

    private final AlumnoInicioService alumnoInicioService;

    @GetMapping("/inicio")
    public AlumnoInicioDTO inicio(
            Authentication authentication
    ) {

        return alumnoInicioService
                .obtenerInicio(
                        authentication.getName()
                );

    }

}