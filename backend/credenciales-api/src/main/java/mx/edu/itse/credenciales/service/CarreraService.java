package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.CarreraDTO;
import mx.edu.itse.credenciales.dto.DashboardCarreraDTO;
import mx.edu.itse.credenciales.entity.Carrera;
import mx.edu.itse.credenciales.repository.AlumnoRepository;
import mx.edu.itse.credenciales.repository.CarreraRepository;
import mx.edu.itse.credenciales.repository.CredencialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarreraService {

    private final CarreraRepository carreraRepository;

    private final AlumnoRepository alumnoRepository;

    private final CredencialRepository credencialRepository;

    //==========================================
    // LISTAR
    //==========================================

    public List<CarreraDTO> obtenerTodas(){

    return carreraRepository.listarConTotalAlumnos();

}

    //==========================================
    // OBTENER
    //==========================================

    public Carrera obtenerPorId(Long id) {

        return carreraRepository

                .findById(id)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Carrera no encontrada."
                        )

                );

    }

    //==========================================
    // REGISTRAR
    //==========================================
public Carrera guardar(Carrera carrera){

    if(carreraRepository.existsByNombre(carrera.getNombre())){

        throw new RuntimeException("La carrera ya existe.");

    }

    carrera.setId(null);

    return carreraRepository.save(carrera);

}

    //==========================================
    // ACTUALIZAR
    //==========================================

    public Carrera actualizar(

            Long id,

            Carrera nueva

    ){

        Carrera carrera = obtenerPorId(id);

        carrera.setNombre(
                nueva.getNombre()
        );

        carrera.setAbreviatura(
                nueva.getAbreviatura()
        );

        return carreraRepository.save(
                carrera
        );

    }

    //==========================================
    // ELIMINAR
    //==========================================

    public void eliminar(Long id){

        if(

                alumnoRepository

                        .existsByCarreraId(id)

        ){

            throw new RuntimeException(

                    "No es posible eliminar la carrera porque tiene alumnos registrados."

            );

        }

        carreraRepository.deleteById(id);

    }

    //==========================================
    // DASHBOARD
    //==========================================

    public DashboardCarreraDTO dashboard(){

        return DashboardCarreraDTO

                .builder()

                .totalCarreras(

                        carreraRepository.count()

                )

                .totalAlumnos(

                        alumnoRepository.count()

                )

                .credencialesActivas(

                        credencialRepository.countByEstado("ACTIVA")

                )

                .build();

    }

}