package mx.edu.itse.credenciales.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_credenciales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialCredencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String evento;

    @Builder.Default
    private LocalDateTime fecha = LocalDateTime.now();

    private String usuario;

    @ManyToOne
    @JoinColumn(name = "credencial_id")
    private Credencial credencial;

}