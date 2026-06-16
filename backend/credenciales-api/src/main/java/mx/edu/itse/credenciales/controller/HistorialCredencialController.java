package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.HistorialCredencial;
import mx.edu.itse.credenciales.service.HistorialCredencialService;
import org.springframework.web.bind.annotation.*;
import mx.edu.itse.credenciales.dto.HistorialResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistorialCredencialController {

    private final HistorialCredencialService historialService;

    @GetMapping("/{credencialId}")
    public List<HistorialResponseDTO> historial(
            @PathVariable Long credencialId
    ) {

        return historialService.obtenerHistorial(
                credencialId
        );

    }

}