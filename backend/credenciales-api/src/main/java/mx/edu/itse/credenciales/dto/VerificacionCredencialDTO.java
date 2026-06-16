package mx.edu.itse.credenciales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificacionCredencialDTO {

    private String nombre;

    private String matricula;

    private String carrera;

    private Integer semestre;

    private String folio;

    private String estado;

    private String fotografia;

}