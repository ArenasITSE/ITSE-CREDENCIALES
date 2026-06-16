package mx.edu.itse.credenciales.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumnoResponseDTO {

    private String mensaje;

    private String usuario;

    private String passwordTemporal;

}