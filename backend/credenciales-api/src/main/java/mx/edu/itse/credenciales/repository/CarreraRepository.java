package mx.edu.itse.credenciales.repository;

import mx.edu.itse.credenciales.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarreraRepository
        extends JpaRepository<Carrera, Long> {
long countByActivoTrue();
}