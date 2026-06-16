package mx.edu.itse.credenciales.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoDTO {

    private String nombreCompleto;

    private String matricula;

    private Integer semestre;

    private Long carreraId;

    private Long usuarioId;

}