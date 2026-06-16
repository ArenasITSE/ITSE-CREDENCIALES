package mx.edu.itse.credenciales.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarreraDTO {

    private Long id;

    private String nombre;

    private String abreviatura;

    private Boolean activo;

}