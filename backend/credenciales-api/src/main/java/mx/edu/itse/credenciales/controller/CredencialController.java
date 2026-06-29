package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.BusquedaCredencialDTO;
import mx.edu.itse.credenciales.dto.DashboardCredencialDTO;
import mx.edu.itse.credenciales.dto.VerificacionCredencialDTO;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.entity.HistorialCredencial;
import mx.edu.itse.credenciales.service.CredencialService;
import mx.edu.itse.credenciales.service.PdfService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/credenciales")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CredencialController {

    private final CredencialService credencialService;
    private final PdfService pdfService;

    //==========================================
    // LISTAR
    //==========================================

    @GetMapping
    public List<Credencial> listar() {

        return credencialService.obtenerTodas();

    }

    //==========================================
    // OBTENER POR ID
    //==========================================

    @GetMapping("/{id}")
    public Credencial obtener(
            @PathVariable Long id
    ) {

        return credencialService.buscarPorId(id);

    }

    //==========================================
    // GENERAR
    //==========================================

    @PostMapping("/generar/{alumnoId}")
    public Credencial generar(
            @PathVariable Long alumnoId
    ) throws Exception {

        return credencialService.generarCredencial(alumnoId);

    }

    //==========================================
    // REGENERAR
    //==========================================

    @PostMapping("/{id}/regenerar")
    public Credencial regenerar(
            @PathVariable Long id
    ) throws Exception {

        return credencialService.regenerarCredencial(id);

    }

    //==========================================
    // ELIMINAR
    //==========================================

    @DeleteMapping("/{id}")
    public String eliminar(
            @PathVariable Long id
    ) {

        credencialService.eliminarCredencial(id);

        return "Credencial eliminada correctamente.";

    }

    //==========================================
    // CANCELAR
    //==========================================

    @PutMapping("/{id}/cancelar")
    public Credencial cancelar(
            @PathVariable Long id
    ) {

        return credencialService.cancelarCredencial(id);

    }

    //==========================================
    // ACTIVAR
    //==========================================

    @PutMapping("/{id}/activar")
    public Credencial activar(
            @PathVariable Long id
    ) {

        return credencialService.activarCredencial(id);

    }

    //==========================================
    // VALIDAR
    //==========================================

    @PutMapping("/{id}/validar")
    public Credencial validar(
            @PathVariable Long id
    ) {

        return credencialService.validarCredencial(id);

    }

    //==========================================
    // BUSCAR
    //==========================================

@GetMapping("/buscar")
public List<Credencial> buscar(

        @RequestParam String texto

){

    return credencialService.buscar(texto);

}

    //==========================================
    // VERIFICAR QR
    //==========================================

    @GetMapping("/verificar/{folio}")
    public VerificacionCredencialDTO verificar(

            @PathVariable String folio

    ) {

        return credencialService.verificar(folio);

    }

    //==========================================
    // DASHBOARD
    //==========================================

    @GetMapping("/dashboard")
    public DashboardCredencialDTO dashboard() {

        return credencialService.dashboard();

    }

    //==========================================
    // HISTORIAL
    //==========================================

    @GetMapping("/{id}/historial")
    public List<HistorialCredencial> historial(

            @PathVariable Long id

    ) {

        return credencialService.obtenerHistorial(id);

    }

    //==========================================
    // VER PDF
    //==========================================

    @GetMapping("/pdf/{id}")
    public ResponseEntity<Resource> verPdf(
            @PathVariable Long id
    ) throws Exception {

        String ruta = pdfService.generarPdf(id);

        Path path = Paths.get(ruta);

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()

                .contentType(MediaType.APPLICATION_PDF)

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"credencial.pdf\""
                )

                .body(resource);

    }

    //==========================================
    // DESCARGAR PDF
    //==========================================

    @GetMapping("/pdf/{id}/download")
    public ResponseEntity<Resource> descargarPdf(
            @PathVariable Long id
    ) throws Exception {

        String ruta = pdfService.generarPdf(id);

        Path path = Paths.get(ruta);

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                path.getFileName() +
                                "\""
                )

                .body(resource);

    }

    //==========================================
    // REGENERAR PDF
    //==========================================

    @PostMapping("/pdf/{id}/regenerar")
    public String regenerarPdf(
            @PathVariable Long id
    ) throws Exception {

        return pdfService.regenerarPdf(id);

    }

}