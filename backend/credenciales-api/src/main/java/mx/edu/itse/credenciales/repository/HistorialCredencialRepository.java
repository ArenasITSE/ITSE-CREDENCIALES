package mx.edu.itse.credenciales.repository;

import mx.edu.itse.credenciales.entity.HistorialCredencial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialCredencialRepository
        extends JpaRepository<HistorialCredencial, Long> {

    List<HistorialCredencial> findByCredencialIdOrderByFechaDesc(Long id);
    void deleteByCredencialId(Long credencialId);

}