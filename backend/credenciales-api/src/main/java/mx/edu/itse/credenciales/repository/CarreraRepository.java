package mx.edu.itse.credenciales.repository;

import mx.edu.itse.credenciales.dto.CarreraDTO;
import mx.edu.itse.credenciales.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarreraRepository
        extends JpaRepository<Carrera,Long>{

    long count();

    Optional<Carrera> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

@Query("""
SELECT new mx.edu.itse.credenciales.dto.CarreraDTO(

    c.id,

    c.nombre,

    c.abreviatura,

    COUNT(a.id)

)

FROM Carrera c

LEFT JOIN Alumno a
ON a.carrera = c

GROUP BY

    c.id,

    c.nombre,

    c.abreviatura

ORDER BY c.nombre

""")
List<CarreraDTO> listarConTotalAlumnos();

}

