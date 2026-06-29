package mx.edu.itse.credenciales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardCredencialDTO {

    private Long activas;

    private Long canceladas;

    private Long validadas;

    private Long porVencer;

}