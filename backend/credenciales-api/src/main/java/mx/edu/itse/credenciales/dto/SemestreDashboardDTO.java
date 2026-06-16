package mx.edu.itse.credenciales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SemestreDashboardDTO {

    private Integer semestre;

    private Long cantidad;

}