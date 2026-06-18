package mx.edu.itse.credenciales.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrarAlumnoResponse {

    private Long id;

    private String usuario;

    private String passwordTemporal;

    private String folio;

    private String mensaje;

}