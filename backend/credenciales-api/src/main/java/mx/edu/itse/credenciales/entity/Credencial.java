package mx.edu.itse.credenciales.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "credenciales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String folio;

    @Column(columnDefinition = "TEXT")
    private String codigoQR;

    private String codigoBarras;

    @Column(length = 500)
    private String qrPath;

    @Builder.Default
    private String estado = "ACTIVA";

    @Builder.Default
    private LocalDateTime fechaGeneracion = LocalDateTime.now();

    @OneToOne
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;
}