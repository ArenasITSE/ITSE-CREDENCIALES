package mx.edu.itse.credenciales.dto;

import lombok.Data;

@Data
public class CambiarPasswordDTO {

    private String passwordActual;

    private String passwordNueva;

    private String confirmarPassword;

}