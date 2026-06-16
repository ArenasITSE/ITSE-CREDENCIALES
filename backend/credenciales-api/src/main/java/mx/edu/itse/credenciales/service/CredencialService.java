package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.VerificacionCredencialDTO;
import mx.edu.itse.credenciales.entity.Alumno;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.repository.AlumnoRepository;
import mx.edu.itse.credenciales.repository.CredencialRepository;
import org.springframework.stereotype.Service;
import mx.edu.itse.credenciales.service.HistorialCredencialService;
import mx.edu.itse.credenciales.dto.BusquedaCredencialDTO;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CredencialService {

    private final CredencialRepository credencialRepository;
    private final AlumnoRepository alumnoRepository;
    private final QrService qrService;
    private final HistorialCredencialService historialService;

    public List<Credencial> obtenerTodas() {
        return credencialRepository.findAll();
    }

    public Credencial generarCredencial(Long alumnoId) throws Exception {

        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() ->
                        new RuntimeException("Alumno no encontrado"));

        if (alumno.getFotografia() == null) {
            throw new RuntimeException(
                    "El alumno no tiene fotografía registrada");
        }

        if (credencialRepository.existsByAlumnoId(alumnoId)) {
            throw new RuntimeException(
                    "El alumno ya tiene una credencial");
        }

        // Generar folio único
        String folio = "ITSE-" + System.currentTimeMillis();

        // URL que abrirá el teléfono al escanear el QR
        String urlVerificacion =
                "http://localhost:8080/verificar/" + folio;

        // Generar imagen QR
        String qrPath = qrService.generarQR(
                urlVerificacion,
                folio
        );

        // Guardar credencial
    Credencial credencial = Credencial.builder()
        .folio(folio)
        .codigoQR(urlVerificacion)
        .codigoBarras(alumno.getMatricula())
        .qrPath(qrPath)
        .alumno(alumno)
        .build();

credencial = credencialRepository.save(credencial);

historialService.registrarEvento(
        credencial,
        "Credencial generada",
        "ADMIN"
);

return credencial;
    }

    public Credencial buscarPorId(Long id) {

        return credencialRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Credencial no encontrada"));
    }

    public Credencial buscarPorFolio(String folio) {

        return credencialRepository.findByFolio(folio)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Credencial no encontrada"));
    }

    public VerificacionCredencialDTO verificar(String folio){

    Credencial credencial = buscarPorFolio(folio);

    return VerificacionCredencialDTO.builder()

            .nombre(
                    credencial.getAlumno().getNombreCompleto()
            )

            .matricula(
                    credencial.getAlumno().getMatricula()
            )

            .carrera(
                    credencial.getAlumno().getCarrera().getNombre()
            )

            .semestre(
                    credencial.getAlumno().getSemestre()
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


// ==========================================
    // CANCELAR CREDENCIAL
    // ==========================================

   public Credencial cancelarCredencial(Long id){

    Credencial credencial =
            credencialRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Credencial no encontrada"
                            ));

    credencial.setEstado("CANCELADA");

    credencial =
            credencialRepository.save(
                    credencial
            );

    historialService.registrarEvento(

            credencial,

            "Credencial cancelada",

            "ADMIN"

    );

    return credencial;

}


public Credencial validarCredencial(Long id){

    Credencial credencial =
            credencialRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Credencial no encontrada"
                            ));

    credencial.setEstado("VALIDADA");

    credencial =
            credencialRepository.save(
                    credencial
            );

    historialService.registrarEvento(

            credencial,

            "Credencial validada",

            "ADMIN"

    );

    return credencial;

}

    // ==========================================
    // ACTIVAR CREDENCIAL
    // ==========================================

    public Credencial activarCredencial(Long id) {

        Credencial credencial = buscarPorId(id);

        if ("ACTIVA".equals(credencial.getEstado())) {

            throw new RuntimeException(
                    "La credencial ya se encuentra activa"
            );

        }

        credencial.setEstado("ACTIVA");

        return credencialRepository.save(
                credencial
        );

    }


    public List<BusquedaCredencialDTO> buscar(String texto){

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


}