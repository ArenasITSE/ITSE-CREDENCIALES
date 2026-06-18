package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.AlumnoActualizarDTO;
import mx.edu.itse.credenciales.dto.AlumnoDTO;
import mx.edu.itse.credenciales.dto.AlumnoResponseDTO;
import mx.edu.itse.credenciales.entity.Alumno;
import mx.edu.itse.credenciales.service.AlumnoService;
import mx.edu.itse.credenciales.service.FotografiaService;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.web.bind.annotation.*;
import mx.edu.itse.credenciales.dto.RegistrarAlumnoRequest;
import mx.edu.itse.credenciales.dto.RegistrarAlumnoResponse;

import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/alumnos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final FotografiaService fotografiaService;

    @GetMapping
    public List<Alumno> listar() {
        return alumnoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Alumno obtenerPorId(@PathVariable Long id) {
        return alumnoService.obtenerPorId(id);
    }

    @PostMapping
    public AlumnoResponseDTO registrar(
            @RequestBody AlumnoDTO alumnoDTO) {

        return alumnoService.registrarAlumno(alumnoDTO);
    }

   @PostMapping(
        value = "/registrar-completo",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
)
public RegistrarAlumnoResponse registrarCompleto(

        @ModelAttribute RegistrarAlumnoRequest request

) throws Exception {

    return alumnoService.registrarCompleto(request);

}

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {

        alumnoService.eliminar(id);

        return "Alumno deshabilitado correctamente";
    }

    @PutMapping("/{id}/foto")
public String actualizarFoto(

        @PathVariable Long id,

        @RequestParam("foto") MultipartFile foto

) throws IOException {

    fotografiaService.subirFoto(id, foto);

    return "Fotografía actualizada correctamente";

}

@PutMapping("/{id}/restablecer-password")
public String restablecerPassword(
        @PathVariable Long id
) {

    String password =
            alumnoService.restablecerPassword(id);

    return "Contraseña restablecida correctamente.\n\nNueva contraseña: "
            + password;

}


@PutMapping("/{id}")
public Alumno actualizar(
        @PathVariable Long id,
        @RequestBody AlumnoActualizarDTO dto
) {

    return alumnoService.actualizarAlumno(
            id,
            dto
    );

}




}