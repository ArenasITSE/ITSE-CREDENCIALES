package mx.edu.itse.credenciales.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusquedaCredencialDTO {

    private Long id;

    private String folio;

    private String nombre;

    private String matricula;

    private String carrera;

    private Integer semestre;

    private String estado;

}