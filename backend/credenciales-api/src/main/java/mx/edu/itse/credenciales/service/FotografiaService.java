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

@Service
@RequiredArgsConstructor
public class FotografiaService {

    private final FotografiaRepository fotografiaRepository;
    private final AlumnoRepository alumnoRepository;

    public Fotografia subirFoto(
        Long alumnoId,
        MultipartFile archivo) throws IOException {

    System.out.println("========== SUBIENDO FOTO ==========");
    System.out.println("Alumno ID: " + alumnoId);
    System.out.println("Archivo: " + archivo.getOriginalFilename());

    Alumno alumno = alumnoRepository.findById(alumnoId)
            .orElseThrow(() ->
                    new RuntimeException("Alumno no encontrado"));

    System.out.println("Alumno encontrado: " + alumno.getNombreCompleto());

    String nombreArchivo =
            System.currentTimeMillis() + "_" +
            archivo.getOriginalFilename();

    Path ruta = Paths.get("uploads");

    if (!Files.exists(ruta)) {
        Files.createDirectories(ruta);
    }

    Files.copy(
            archivo.getInputStream(),
            ruta.resolve(nombreArchivo),
            StandardCopyOption.REPLACE_EXISTING
    );

    System.out.println("Archivo copiado.");

    Fotografia fotografia = Fotografia.builder()
            .nombreArchivo(nombreArchivo)
            .ruta("uploads/" + nombreArchivo)
            .build();

    fotografia = fotografiaRepository.save(fotografia);

    System.out.println("Fotografía guardada en BD. ID: " + fotografia.getId());

    alumno.setFotografia(fotografia);

    alumnoRepository.save(alumno);

    System.out.println("Alumno actualizado.");

    return fotografia;
}
}