package mx.edu.itse.credenciales.repository;

import mx.edu.itse.credenciales.dto.CarreraDashboardDTO;
import mx.edu.itse.credenciales.dto.SemestreDashboardDTO;
import mx.edu.itse.credenciales.entity.Alumno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository
        extends JpaRepository<Alumno, Long> {

                

    //========================================
    // LOGIN / PERFIL
    //========================================

    Optional<Alumno> findByUsuarioUsername(
            String username
    );

    //========================================
    // VALIDACIONES
    //========================================

    boolean existsByMatricula(
            String matricula
    );

    Optional<Alumno> findByMatricula(
            String matricula
    );

    //========================================
    // DASHBOARD
    //========================================

    long countByActivoTrue();

    @Query("""
    SELECT new mx.edu.itse.credenciales.dto.CarreraDashboardDTO(
            c.nombre,
            COUNT(a)
    )
    FROM Alumno a
    JOIN a.carrera c
    WHERE a.activo = true
    GROUP BY c.nombre
    ORDER BY COUNT(a) DESC
    """)
    List<CarreraDashboardDTO> obtenerAlumnosPorCarrera();

    @Query("""
    SELECT new mx.edu.itse.credenciales.dto.SemestreDashboardDTO(
            a.semestre,
            COUNT(a)
    )
    FROM Alumno a
    WHERE a.activo = true
    GROUP BY a.semestre
    ORDER BY a.semestre
    """)
    List<SemestreDashboardDTO> obtenerAlumnosPorSemestre();

    //========================================
    // ÚLTIMOS REGISTROS
    //========================================

    List<Alumno> findTop5ByOrderByIdDesc();

    Alumno findTopByOrderByIdDesc();

    Optional<Alumno> findByFotografiaId(Long fotografiaId);

    boolean existsByCarreraId(Long carreraId);

}