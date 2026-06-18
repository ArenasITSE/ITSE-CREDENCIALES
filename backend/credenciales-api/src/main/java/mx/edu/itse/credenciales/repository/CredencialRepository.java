package mx.edu.itse.credenciales.repository;

import mx.edu.itse.credenciales.dto.EstadoCredencialDashboardDTO;
import mx.edu.itse.credenciales.entity.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CredencialRepository
        extends JpaRepository<Credencial, Long> {

    // ==========================================
    // CONSULTAS BÁSICAS
    // ==========================================

    Optional<Credencial> findByAlumnoId(Long alumnoId);

    boolean existsByAlumnoId(Long alumnoId);

    Optional<Credencial> findByFolio(String folio);

    long count();

    long countByEstado(String estado);

    Optional<Credencial> findByAlumnoUsuarioUsername(
            String username
    );

    Credencial findTopByOrderByIdDesc();
    // ==========================================
    // DASHBOARD
    // ==========================================

    @Query("""

        SELECT new mx.edu.itse.credenciales.dto.EstadoCredencialDashboardDTO(

            c.estado,

            COUNT(c)

        )

        FROM Credencial c

        GROUP BY c.estado

        ORDER BY c.estado

    """)
    List<EstadoCredencialDashboardDTO> obtenerCredencialesPorEstado();

    List<Credencial> findTop5ByOrderByFechaGeneracionDesc();

    Credencial findTopByOrderByFechaGeneracionDesc();

    // ==========================================
    // PERFIL DEL ALUMNO
    // ==========================================

    @Query("""

        SELECT c

        FROM Credencial c

        JOIN c.alumno a

        JOIN a.usuario u

        WHERE u.username = :username

    """)
    Optional<Credencial> obtenerPorUsername(
            @Param("username") String username
    );

    // ==========================================
    // BUSCADOR
    // ==========================================

    @Query("""

        SELECT c

        FROM Credencial c

        WHERE

            LOWER(c.alumno.nombreCompleto)
            LIKE LOWER(CONCAT('%', :texto, '%'))

            OR

            LOWER(c.alumno.matricula)
            LIKE LOWER(CONCAT('%', :texto, '%'))

            OR

            LOWER(c.folio)
            LIKE LOWER(CONCAT('%', :texto, '%'))

            OR

            LOWER(c.alumno.carrera.nombre)
            LIKE LOWER(CONCAT('%', :texto, '%'))

        ORDER BY c.alumno.nombreCompleto

    """)
    List<Credencial> buscar(
            @Param("texto") String texto
    );

}