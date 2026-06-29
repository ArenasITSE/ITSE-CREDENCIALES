package mx.edu.itse.credenciales.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardCarreraDTO {

    private Long totalCarreras;

    private Long totalAlumnos;

    private Long credencialesActivas;

}