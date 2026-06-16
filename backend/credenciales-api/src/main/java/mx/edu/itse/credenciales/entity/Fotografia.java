package mx.edu.itse.credenciales.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fotografias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fotografia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreArchivo;

    private String ruta;

    @Builder.Default
    private LocalDateTime fechaSubida = LocalDateTime.now();

}