package mx.edu.itse.credenciales.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UltimaCredencialDTO {

    private String nombre;

    private String folio;

    private LocalDateTime fecha;

}