package mx.edu.itse.credenciales.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HistorialResponseDTO {

    private Long id;

    private String evento;

    private String usuario;

    private LocalDateTime fecha;

}