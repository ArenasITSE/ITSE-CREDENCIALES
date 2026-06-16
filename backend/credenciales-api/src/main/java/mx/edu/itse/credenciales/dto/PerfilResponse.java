package mx.edu.itse.credenciales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilResponse {

    private String matricula;
    private String nombreCompleto;
    private Integer semestre;
    private String carrera;
    private String fotografia;
}