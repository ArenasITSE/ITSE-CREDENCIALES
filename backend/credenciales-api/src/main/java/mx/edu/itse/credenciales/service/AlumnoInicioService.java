package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.AlumnoInicioDTO;
import mx.edu.itse.credenciales.entity.Alumno;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.repository.AlumnoRepository;
import mx.edu.itse.credenciales.repository.CredencialRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlumnoInicioService {

    private final AlumnoRepository alumnoRepository;
    private final CredencialRepository credencialRepository;

    public AlumnoInicioDTO obtenerInicio(
            String username
    ) {

        Alumno alumno =
                alumnoRepository
                        .findByUsuarioUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Alumno no encontrado"
                                ));

        Optional<Credencial> credencialOpt =
                credencialRepository
                        .obtenerPorUsername(username);

        AlumnoInicioDTO.AlumnoInicioDTOBuilder dto =
                AlumnoInicioDTO.builder()

                        .nombreCompleto(
                                alumno.getNombreCompleto()
                        )

                        .matricula(
                                alumno.getMatricula()
                        )

                        .carrera(
                                alumno.getCarrera().getNombre()
                        )

                        .semestre(
                                alumno.getSemestre()
                        )

                        .fotografia(

                                alumno.getFotografia() != null
                                        ? alumno.getFotografia().getRuta()
                                        : null

                        );

        if (credencialOpt.isPresent()) {

            Credencial credencial =
                    credencialOpt.get();

            dto

                    .credencialId(
                            credencial.getId()
                    )

                    .folio(
                            credencial.getFolio()
                    )

                    .estado(
                            credencial.getEstado()
                    )

                    .fechaGeneracion(
                            credencial.getFechaGeneracion()
                    )

                    .pdf(

                            "/api/credenciales/pdf/"
                                    + credencial.getId()
                                    + "/download"

                    );

        }

        return dto.build();

    }

}