package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Fotografia;
import mx.edu.itse.credenciales.service.FotografiaService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/fotografias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FotografiaController {

    private final FotografiaService fotografiaService;

    @PostMapping(
            value = "/subir",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Fotografia subirFoto(

            @RequestParam Long alumnoId,

            @RequestParam MultipartFile foto

    ) throws Exception {

        return fotografiaService.subirFoto(
                alumnoId,
                foto
        );
    }

    @PostMapping("/test")
public String test(
        @RequestParam("foto") MultipartFile foto
) {
    return foto.getOriginalFilename();
}
}

