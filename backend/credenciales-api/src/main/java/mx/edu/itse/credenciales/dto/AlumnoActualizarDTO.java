package mx.edu.itse.credenciales.dto;

import lombok.Data;

@Data
public class AlumnoActualizarDTO {

    private String nombreCompleto;

    private Integer semestre;

    private Long carreraId;

}