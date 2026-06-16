package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;

import mx.edu.itse.credenciales.dto.DashboardDTO;
import mx.edu.itse.credenciales.dto.UltimaCredencialDTO;
import mx.edu.itse.credenciales.dto.UltimoAlumnoDTO;

import mx.edu.itse.credenciales.repository.AlumnoRepository;
import mx.edu.itse.credenciales.repository.CarreraRepository;
import mx.edu.itse.credenciales.repository.CredencialRepository;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AlumnoRepository alumnoRepository;
    private final CarreraRepository carreraRepository;
    private final CredencialRepository credencialRepository;

    public DashboardDTO obtenerDashboard() {

        return DashboardDTO.builder()

                // ===========================
                // TARJETAS PRINCIPALES
                // ===========================

                .totalAlumnos(
                        alumnoRepository.countByActivoTrue()
                )

                .totalCarreras(
                        carreraRepository.countByActivoTrue()
                )

                .totalCredenciales(
                        credencialRepository.count()
                )

                .credencialesValidadas(
                        credencialRepository.countByEstado("VALIDADA")
                )

                .credencialesCanceladas(
                        credencialRepository.countByEstado("CANCELADA")
                )

                // ===========================
                // GRÁFICAS
                // ===========================

                .alumnosPorCarrera(
                        alumnoRepository.obtenerAlumnosPorCarrera()
                )

                .alumnosPorSemestre(
                        alumnoRepository.obtenerAlumnosPorSemestre()
                )

                .credencialesPorEstado(
                        credencialRepository.obtenerCredencialesPorEstado()
                )

                // ===========================
                // ÚLTIMAS CREDENCIALES
                // ===========================

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

                // ===========================
                // ÚLTIMOS ALUMNOS
                // ===========================

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