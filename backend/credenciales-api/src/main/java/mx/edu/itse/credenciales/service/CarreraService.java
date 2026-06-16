package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Carrera;
import mx.edu.itse.credenciales.repository.CarreraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarreraService {

    private final CarreraRepository carreraRepository;

    public List<Carrera> obtenerTodas() {
        return carreraRepository.findAll();
    }

    public Carrera guardar(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    public Carrera obtenerPorId(Long id) {
        return carreraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));
    }

    public Carrera actualizar(Long id, Carrera carreraActualizada) {

        Carrera carrera = obtenerPorId(id);

        carrera.setNombre(carreraActualizada.getNombre());
        carrera.setAbreviatura(carreraActualizada.getAbreviatura());
        carrera.setActivo(carreraActualizada.getActivo());

        return carreraRepository.save(carrera);
    }

    public void eliminar(Long id) {

        Carrera carrera = obtenerPorId(id);

        carreraRepository.delete(carrera);
    }
}