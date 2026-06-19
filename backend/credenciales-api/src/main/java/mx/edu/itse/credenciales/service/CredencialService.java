package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.BusquedaCredencialDTO;
import mx.edu.itse.credenciales.dto.VerificacionCredencialDTO;
import mx.edu.itse.credenciales.entity.Alumno;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.repository.AlumnoRepository;
import mx.edu.itse.credenciales.repository.CredencialRepository;
import org.springframework.stereotype.Service;
import mx.edu.itse.credenciales.repository.HistorialCredencialRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CredencialService {

    //=========================================
    // REPOSITORIES
    //=========================================

    private final CredencialRepository credencialRepository;

    private final AlumnoRepository alumnoRepository;
    private final HistorialCredencialRepository historialRepository;

    //=========================================
    // SERVICES
    //=========================================

    private final QrService qrService;

    private final HistorialCredencialService historialService;

    private final FolioGenerator folioGenerator;

    //=========================================
    // OBTENER TODAS
    //=========================================

    public List<Credencial> obtenerTodas() {

        return credencialRepository.findAll();

    }

        //=========================================
    // GENERAR CREDENCIAL POR ID
    //=========================================

    public Credencial generarCredencial(
            Long alumnoId
    ) throws Exception {

        Alumno alumno =

                alumnoRepository.findById(
                        alumnoId
                )

                .orElseThrow(() ->

                        new RuntimeException(
                                "Alumno no encontrado"
                        )

                );

        return generarCredencial(
                alumno
        );

    }

    //=========================================
    // GENERAR CREDENCIAL
    //=========================================

    public Credencial generarCredencial(
            Alumno alumno
    ) throws Exception {

        if (alumno.getFotografia() == null) {

            throw new RuntimeException(
                    "El alumno no tiene fotografía registrada"
            );

        }

        if (credencialRepository.existsByAlumnoId(
                alumno.getId()
        )) {

            throw new RuntimeException(
                    "El alumno ya tiene una credencial"
            );

        }

        String folio =

                folioGenerator.generar();

        String urlVerificacion =

                "http://localhost:8080/verificar/"
                        + folio;

        String qrPath =

                qrService.generarQR(

                        urlVerificacion,

                        folio

                );

        Credencial credencial =

                Credencial.builder()

                        .folio(
                                folio
                        )

                        .codigoQR(
                                urlVerificacion
                        )

                        .codigoBarras(
                                alumno.getMatricula()
                        )

                        .qrPath(
                                qrPath
                        )

                        .estado(
                                "ACTIVA"
                        )

                        .alumno(
                                alumno
                        )

                        .build();

        credencial =

                credencialRepository.save(
                        credencial
                );

        historialService.registrarEvento(

                credencial,

                "Credencial generada",

                "ADMIN"

        );

        return credencial;

    }

        //=========================================
    // BUSCAR POR ID
    //=========================================

    public Credencial buscarPorId(Long id) {

        return credencialRepository.findById(id)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Credencial no encontrada"
                        )

                );

    }

    //=========================================
    // BUSCAR POR FOLIO
    //=========================================

    public Credencial buscarPorFolio(String folio) {

        return credencialRepository.findByFolio(folio)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Credencial no encontrada"
                        )

                );

    }

    //=========================================
    // VERIFICAR CREDENCIAL
    //=========================================

    public VerificacionCredencialDTO verificar(
            String folio
    ) {

        Credencial credencial =
                buscarPorFolio(folio);

        return VerificacionCredencialDTO.builder()

                .nombre(
                        credencial.getAlumno()
                                .getNombreCompleto()
                )

                .matricula(
                        credencial.getAlumno()
                                .getMatricula()
                )

                .carrera(
                        credencial.getAlumno()
                                .getCarrera()
                                .getNombre()
                )

                .semestre(
                        credencial.getAlumno()
                                .getSemestre()
                )

                .folio(
                        credencial.getFolio()
                )

                .estado(
                        credencial.getEstado()
                )

                .fotografia(
                        credencial.getAlumno()
                                .getFotografia()
                                .getRuta()
                )

                .build();

    }



  //=========================================
// CANCELAR CREDENCIAL
//=========================================

public Credencial cancelarCredencial(Long id) {

    Credencial credencial = buscarPorId(id);

    credencial.setEstado("CANCELADA");

    credencial = credencialRepository.save(credencial);

    historialService.registrarEvento(
            credencial,
            "Credencial cancelada",
            "ADMIN"
    );

    return credencial;
}

//=========================================
// VALIDAR CREDENCIAL
//=========================================

public Credencial validarCredencial(Long id) {

    Credencial credencial = buscarPorId(id);

    credencial.setEstado("VALIDADA");

    credencial = credencialRepository.save(credencial);

    historialService.registrarEvento(
            credencial,
            "Credencial validada",
            "ADMIN"
    );

    return credencial;
}

//=========================================
// ACTIVAR CREDENCIAL
//=========================================

public Credencial activarCredencial(Long id) {

    Credencial credencial = buscarPorId(id);

    if ("ACTIVA".equals(credencial.getEstado())) {
        throw new RuntimeException(
                "La credencial ya se encuentra activa"
        );
    }

    credencial.setEstado("ACTIVA");

    credencial = credencialRepository.save(credencial);

    historialService.registrarEvento(
            credencial,
            "Credencial activada",
            "ADMIN"
    );

    return credencial;
}

    //=========================================
    // BUSCAR CREDENCIALES
    //=========================================

    public List<BusquedaCredencialDTO> buscar(
            String texto
    ) {

        return credencialRepository

                .buscar(texto)

                .stream()

                .map(c ->

                        BusquedaCredencialDTO.builder()

                                .id(
                                        c.getId()
                                )

                                .folio(
                                        c.getFolio()
                                )

                                .nombre(
                                        c.getAlumno()
                                                .getNombreCompleto()
                                )

                                .matricula(
                                        c.getAlumno()
                                                .getMatricula()
                                )

                                .carrera(
                                        c.getAlumno()
                                                .getCarrera()
                                                .getNombre()
                                )

                                .semestre(
                                        c.getAlumno()
                                                .getSemestre()
                                )

                                .estado(
                                        c.getEstado()
                                )

                                .build()

                )

                .toList();

    }

    
    //=========================================
// ELIMINAR CREDENCIAL
//=========================================

public void eliminarCredencial(Long id) {

    Credencial credencial = buscarPorId(id);

    historialRepository.deleteByCredencialId(id);

    if (credencial.getQrPath() != null) {

        try {

            Files.deleteIfExists(

                    Paths.get(
                            credencial.getQrPath()
                    )

            );

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    credencialRepository.delete(credencial);

}

//=========================================
// REGENERAR CREDENCIAL
//=========================================

public Credencial regenerarCredencial(Long id) throws Exception {

    Credencial anterior = buscarPorId(id);

    Alumno alumno = anterior.getAlumno();

    eliminarCredencial(id);

    return generarCredencial(alumno);

}

}
