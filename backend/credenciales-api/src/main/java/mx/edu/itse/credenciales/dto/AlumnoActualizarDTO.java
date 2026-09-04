package mx.edu.itse.credenciales.dto;

import lombok.Data;

@Data
public class AlumnoActualizarDTO {

    private String nombreCompleto;

    private String matricula;

    private Integer semestre;

    private Long carreraId;

}