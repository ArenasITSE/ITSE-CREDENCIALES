package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.service.CredencialService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class VerificacionController {

    private final CredencialService credencialService;

    @GetMapping("/verificar/{folio}")
    public String verificar(
            @PathVariable String folio,
            Model model){

        Credencial credencial =
                credencialService.buscarPorFolio(folio);

        model.addAttribute(
                "alumno",
                credencial.getAlumno().getNombreCompleto()
        );

        model.addAttribute(
                "matricula",
                credencial.getAlumno().getMatricula()
        );

        model.addAttribute(
                "carrera",
                credencial.getAlumno().getCarrera().getNombre()
        );

        model.addAttribute(
                "semestre",
                credencial.getAlumno().getSemestre()
        );

        model.addAttribute(
                "estado",
                credencial.getEstado()
        );

        model.addAttribute(
                "folio",
                credencial.getFolio()
        );

        model.addAttribute(
                "foto",
                credencial.getAlumno()
                        .getFotografia()
                        .getRuta()
        );

        return "verificar";
    }

}