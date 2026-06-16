package mx.edu.itse.credenciales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadoCredencialDashboardDTO {

    private String estado;

    private Long cantidad;

}