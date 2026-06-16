package mx.edu.itse.credenciales.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UltimoAlumnoDTO {

    private String nombre;

    private String matricula;

    private Integer semestre;

}