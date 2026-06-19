package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Alumno;
import mx.edu.itse.credenciales.entity.Fotografia;
import mx.edu.itse.credenciales.repository.AlumnoRepository;
import mx.edu.itse.credenciales.repository.FotografiaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FotografiaService {

    private static final String CARPETA = "uploads";

    private final FotografiaRepository fotografiaRepository;
    private final AlumnoRepository alumnoRepository;

    //=========================================
    // SUBIR FOTO PARA UN ALUMNO
    //=========================================

    public Fotografia subirFoto(

            Long alumnoId,

            MultipartFile archivo

    ) throws IOException {

        Alumno alumno = alumnoRepository

                .findById(alumnoId)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Alumno no encontrado"
                        )

                );

        Fotografia fotografia = guardarFotografia(
                archivo
        );

        alumno.setFotografia(
                fotografia
        );

        alumnoRepository.save(
                alumno
        );

        return fotografia;

    }

    //=========================================
    // GUARDAR FOTOGRAFÍA
    //=========================================

    public Fotografia guardarFotografia(

            MultipartFile archivo

    ) throws IOException {
                if (archivo == null || archivo.isEmpty()) {

            throw new RuntimeException(
                    "Debe seleccionar una fotografía."
            );

        }

        Path carpeta = Paths.get(CARPETA);

        if (!Files.exists(carpeta)) {

            Files.createDirectories(carpeta);

        }

        String extension = obtenerExtension(
                archivo.getOriginalFilename()
        );

        String nombreArchivo =

                UUID.randomUUID()

                        + "."

                        + extension;

        Path destino =

                carpeta.resolve(
                        nombreArchivo
                );

        Files.copy(

                archivo.getInputStream(),

                destino,

                StandardCopyOption.REPLACE_EXISTING

        );

        Fotografia fotografia =

                Fotografia.builder()

                        .nombreArchivo(
                                nombreArchivo
                        )

                        .ruta(
                                CARPETA + "/" + nombreArchivo
                        )

                        .build();

        return fotografiaRepository.save(
                fotografia
        );

    }

    //=========================================
    // OBTENER EXTENSIÓN
    //=========================================

    private String obtenerExtension(
            String nombre
    ) {

        if (nombre == null) {

            return "jpg";

        }

        int punto = nombre.lastIndexOf(".");

        if (punto == -1) {

            return "jpg";

        }

        return nombre.substring(
                punto + 1
        );

    }

    //=========================================
    // ELIMINAR FOTOGRAFÍA
    //=========================================

    public void eliminarFoto(
            Long fotografiaId
    ) {
                Fotografia fotografia =

                fotografiaRepository

                        .findById(fotografiaId)

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Fotografía no encontrada"
                                )

                        );

        //=========================================
        // DESVINCULAR DEL ALUMNO
        //=========================================

        Optional<Alumno> alumnoOpt =

                alumnoRepository.findByFotografiaId(
                        fotografiaId
                );

        if (alumnoOpt.isPresent()) {

            Alumno alumno = alumnoOpt.get();

            alumno.setFotografia(null);

            alumnoRepository.save(
                    alumno
            );

        }

        //=========================================
        // ELIMINAR ARCHIVO FÍSICO
        //=========================================

        try {

            File archivo = new File(
                    fotografia.getRuta()
            );

            if (archivo.exists()) {

                archivo.delete();

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        fotografiaRepository.delete(
                fotografia
        );

    }

    //=========================================
    // REEMPLAZAR FOTOGRAFÍA
    //=========================================

    public Fotografia reemplazarFoto(

            Long alumnoId,

            MultipartFile archivo

    ) throws IOException {
                Alumno alumno =

                alumnoRepository

                        .findById(alumnoId)

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Alumno no encontrado"
                                )

                        );

        //=========================================
        // GUARDAR REFERENCIA DE LA FOTO ANTERIOR
        //=========================================

        Fotografia fotoAnterior =
                alumno.getFotografia();

        //=========================================
        // GUARDAR NUEVA FOTOGRAFÍA
        //=========================================

        Fotografia fotografia =

                guardarFotografia(
                        archivo
                );

        //=========================================
        // ACTUALIZAR ALUMNO
        //=========================================

        alumno.setFotografia(
                fotografia
        );

        alumnoRepository.save(
                alumno
        );

        //=========================================
        // ELIMINAR FOTO ANTERIOR
        //=========================================

        if (fotoAnterior != null) {

            eliminarFoto(
                    fotoAnterior.getId()
            );

        }

        return fotografia;

    }
        //=========================================
    // EXISTE FOTOGRAFÍA
    //=========================================

    public boolean existeFotografia(
            Long fotografiaId
    ) {

        return fotografiaRepository.existsById(
                fotografiaId
        );

    }

    //=========================================
    // OBTENER FOTOGRAFÍA
    //=========================================

    public Fotografia obtenerPorId(
            Long fotografiaId
    ) {

        return fotografiaRepository

                .findById(fotografiaId)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Fotografía no encontrada"
                        )

                );

    }

}
