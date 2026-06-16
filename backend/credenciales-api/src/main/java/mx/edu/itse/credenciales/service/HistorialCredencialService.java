package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.entity.HistorialCredencial;
import mx.edu.itse.credenciales.repository.CredencialRepository;
import mx.edu.itse.credenciales.repository.HistorialCredencialRepository;
import org.springframework.stereotype.Service;
import mx.edu.itse.credenciales.dto.HistorialResponseDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistorialCredencialService {

    private final HistorialCredencialRepository historialRepository;
    private final CredencialRepository credencialRepository;

    /**
     * Guarda un evento en el historial
     */
    public void registrarEvento(
            Credencial credencial,
            String evento,
            String usuario
    ) {

        HistorialCredencial historial =
                HistorialCredencial.builder()
                        .credencial(credencial)
                        .evento(evento)
                        .usuario(usuario)
                        .build();

        historialRepository.save(historial);

    }

    /**
     * Obtiene el historial completo de una credencial
     */
public List<HistorialResponseDTO> obtenerHistorial(
        Long credencialId
) {

    return historialRepository
            .findByCredencialIdOrderByFechaDesc(
                    credencialId
            )
            .stream()
            .map(historial -> HistorialResponseDTO.builder()

                    .id(historial.getId())

                    .evento(historial.getEvento())

                    .usuario(historial.getUsuario())

                    .fecha(historial.getFecha())

                    .build()

            )
            .toList();

}

}