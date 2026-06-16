package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.VerificacionCredencialDTO;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.service.CredencialService;
import mx.edu.itse.credenciales.service.PdfService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import mx.edu.itse.credenciales.dto.BusquedaCredencialDTO;
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

    // ==========================================
    // LISTAR CREDENCIALES
    // ==========================================

    @GetMapping
    public List<Credencial> listar() {

        return credencialService.obtenerTodas();

    }

    // ==========================================
    // GENERAR CREDENCIAL
    // ==========================================

    @PostMapping("/generar/{alumnoId}")
    public Credencial generar(
            @PathVariable Long alumnoId) {

        try {

            return credencialService.generarCredencial(alumnoId);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error generando credencial",
                    e
            );

        }

    }

    // ==========================================
    // VERIFICAR CREDENCIAL
    // ==========================================

    @GetMapping("/verificar/{folio}")
    public VerificacionCredencialDTO verificar(
            @PathVariable String folio) {

        return credencialService.verificar(folio);

    }

    // ==========================================
    // GENERAR PDF
    // ==========================================

    @GetMapping("/pdf/{id}")
    public String generarPdf(
            @PathVariable Long id
    ) throws Exception {

        return pdfService.generarPdf(id);

    }

    // ==========================================
    // DESCARGAR PDF
    // ==========================================

    @GetMapping("/pdf/{id}/download")
    public ResponseEntity<Resource> descargarPdf(
            @PathVariable Long id
    ) throws Exception {

        String rutaPdf = pdfService.generarPdf(id);

        Path path = Paths.get(rutaPdf);

        Resource resource =
                new UrlResource(path.toUri());

        return ResponseEntity.ok()

                .header(

                        HttpHeaders.CONTENT_DISPOSITION,

                        "attachment; filename=\""
                                + path.getFileName().toString()
                                + "\""

                )

                .body(resource);

    }

    // ==========================================
    // REGENERAR PDF
    // ==========================================

    @PostMapping("/pdf/{id}/regenerar")
    public String regenerarPdf(
            @PathVariable Long id
    ) throws Exception {

        return pdfService.regenerarPdf(id);

    }

    // ==========================================
    // CANCELAR CREDENCIAL
    // ==========================================

    @PutMapping("/{id}/cancelar")
    public Credencial cancelarCredencial(
            @PathVariable Long id
    ) {

        return credencialService.cancelarCredencial(id);

    }

    // ==========================================
    // ACTIVAR / VALIDAR CREDENCIAL
    // ==========================================

    @PutMapping("/{id}/activar")
    public Credencial activarCredencial(
            @PathVariable Long id
    ) {

        return credencialService.activarCredencial(id);

    }


    @GetMapping("/buscar")
public List<BusquedaCredencialDTO> buscar(

        @RequestParam String texto

){

    return credencialService.buscar(texto);

}
}