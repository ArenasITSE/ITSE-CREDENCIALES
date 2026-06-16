package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Carrera;
import mx.edu.itse.credenciales.service.CarreraService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carreras")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CarreraController {

    private final CarreraService carreraService;

    @GetMapping
    public List<Carrera> listar() {
        return carreraService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Carrera obtenerPorId(@PathVariable Long id) {
        return carreraService.obtenerPorId(id);
    }

    @PostMapping
    public Carrera guardar(@RequestBody Carrera carrera) {
        return carreraService.guardar(carrera);
    }

    @PutMapping("/{id}")
    public Carrera actualizar(
            @PathVariable Long id,
            @RequestBody Carrera carrera) {

        return carreraService.actualizar(id, carrera);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        carreraService.eliminar(id);
    }
}