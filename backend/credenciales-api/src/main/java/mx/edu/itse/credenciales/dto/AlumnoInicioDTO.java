package mx.edu.itse.credenciales.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoInicioDTO {

    private String nombreCompleto;

    private String matricula;

    private String carrera;

    private Integer semestre;

    private String fotografia;

    private Long credencialId;

    private String folio;

    private String estado;

    private LocalDateTime fechaGeneracion;

    private String pdf;

}