package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.DashboardDTO;
import mx.edu.itse.credenciales.dto.UltimaCredencialDTO;
import mx.edu.itse.credenciales.dto.UltimoAlumnoDTO;
import mx.edu.itse.credenciales.entity.Alumno;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.repository.AlumnoRepository;
import mx.edu.itse.credenciales.repository.CarreraRepository;
import mx.edu.itse.credenciales.repository.CredencialRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AlumnoRepository alumnoRepository;
    private final CarreraRepository carreraRepository;
    private final CredencialRepository credencialRepository;

    public DashboardDTO obtenerDashboard() {

        // ==========================================
        // ÚLTIMO ALUMNO
        // ==========================================

        Alumno ultimoAlumno =
                alumnoRepository.findTopByOrderByIdDesc();

        // ==========================================
        // ÚLTIMA CREDENCIAL
        // ==========================================

        Credencial ultimaCredencial =
                credencialRepository.findTopByOrderByFechaGeneracionDesc();

        return DashboardDTO.builder()

                // ==========================================
                // TARJETAS
                // ==========================================

                .totalAlumnos(
                        alumnoRepository.countByActivoTrue()
                )

              .credencialesActivas(

        credencialRepository.count()

)
                .credencialesCanceladas(
                        credencialRepository.countByEstado("CANCELADA")
                )

                .totalCarreras(
                        carreraRepository.count()
                )

                // ==========================================
                // RESUMEN
                // ==========================================

                .ultimoAlumno(

                        ultimoAlumno != null

                                ? ultimoAlumno.getNombreCompleto()

                                : "--"

                )

                .ultimaCredencial(

                        ultimaCredencial != null

                                ? ultimaCredencial.getFolio()

                                : "--"

                )

                .ultimaActualizacion(

                        LocalDateTime.now()

                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "dd/MM/yyyy HH:mm"
                                        )
                                )

                )

                // ==========================================
                // GRÁFICAS
                // ==========================================

                .alumnosPorCarrera(
                        alumnoRepository.obtenerAlumnosPorCarrera()
                )

                .alumnosPorSemestre(
                        alumnoRepository.obtenerAlumnosPorSemestre()
                )

                .credencialesPorEstado(
                        credencialRepository.obtenerCredencialesPorEstado()
                )

                // ==========================================
                // ÚLTIMAS CREDENCIALES
                // ==========================================

                .ultimasCredenciales(

                        credencialRepository

                                .findTop5ByOrderByFechaGeneracionDesc()

                                .stream()

                                .map(c ->

                                        UltimaCredencialDTO.builder()

                                                .nombre(
                                                        c.getAlumno()
                                                                .getNombreCompleto()
                                                )

                                                .folio(
                                                        c.getFolio()
                                                )

                                                .fecha(
                                                        c.getFechaGeneracion()
                                                )

                                                .build()

                                )

                                .toList()

                )

                // ==========================================
                // ÚLTIMOS ALUMNOS
                // ==========================================

                .ultimosAlumnos(

                        alumnoRepository

                                .findTop5ByOrderByIdDesc()

                                .stream()

                                .map(a ->

                                        UltimoAlumnoDTO.builder()

                                                .nombre(
                                                        a.getNombreCompleto()
                                                )

                                                .matricula(
                                                        a.getMatricula()
                                                )

                                                .semestre(
                                                        a.getSemestre()
                                                )

                                                .build()

                                )

                                .toList()

                )

                .build();

    }

}