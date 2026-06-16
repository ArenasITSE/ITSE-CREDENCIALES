package mx.edu.itse.credenciales.repository;

import mx.edu.itse.credenciales.entity.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;
import mx.edu.itse.credenciales.dto.EstadoCredencialDashboardDTO;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface CredencialRepository
        extends JpaRepository<Credencial, Long> {

    Optional<Credencial> findByAlumnoId(Long alumnoId);

    boolean existsByAlumnoId(Long alumnoId);

    Optional<Credencial> findByFolio(String folio);

    long count();

long countByEstado(String estado);

@Query("""
SELECT new mx.edu.itse.credenciales.dto.EstadoCredencialDashboardDTO(

c.estado,

COUNT(c)

)

FROM Credencial c

GROUP BY c.estado

""")
List<EstadoCredencialDashboardDTO> obtenerCredencialesPorEstado();
List<Credencial> findTop5ByOrderByFechaGeneracionDesc();
Optional<Credencial> findById(Long id);

@Query("""

SELECT c

FROM Credencial c

WHERE

LOWER(c.alumno.nombreCompleto)

LIKE LOWER(CONCAT('%',:texto,'%'))

OR

LOWER(c.alumno.matricula)

LIKE LOWER(CONCAT('%',:texto,'%'))

OR

LOWER(c.folio)

LIKE LOWER(CONCAT('%',:texto,'%'))

OR

LOWER(c.alumno.carrera.nombre)

LIKE LOWER(CONCAT('%',:texto,'%'))

ORDER BY c.alumno.nombreCompleto

""")
List<Credencial> buscar(
        @Param("texto") String texto
);


}