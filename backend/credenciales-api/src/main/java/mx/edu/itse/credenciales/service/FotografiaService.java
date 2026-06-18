package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Alumno;
import mx.edu.itse.credenciales.entity.Fotografia;
import mx.edu.itse.credenciales.repository.AlumnoRepository;
import mx.edu.itse.credenciales.repository.FotografiaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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

        Alumno alumno =
                alumnoRepository.findById(alumnoId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Alumno no encontrado"
                                ));

        Fotografia fotografia =
                guardarFotografia(archivo);

        alumno.setFotografia(
                fotografia
        );

        alumnoRepository.save(
                alumno
        );

        return fotografia;

    }

    //=========================================
    // GUARDAR FOTO
    //=========================================

    public Fotografia guardarFotografia(

            MultipartFile archivo

    ) throws IOException {

        if (archivo == null || archivo.isEmpty()) {

            throw new RuntimeException(
                    "Debe seleccionar una fotografía."
            );

        }

        Path carpeta =
                Paths.get(CARPETA);

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
    // EXTENSIÓN
    //=========================================

    private String obtenerExtension(
            String nombre
    ) {

        if (nombre == null) {

            return "jpg";

        }

        int punto =
                nombre.lastIndexOf(".");

        if (punto == -1) {

            return "jpg";

        }

        return nombre.substring(
                punto + 1
        );

    }

}