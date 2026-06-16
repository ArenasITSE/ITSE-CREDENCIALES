package mx.edu.itse.credenciales.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alumnos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false)
    private Integer semestre;

    @Builder.Default
    private Boolean activo = true;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "carrera_id")
    private Carrera carrera;

    @OneToOne
    @JoinColumn(name = "fotografia_id")
    private Fotografia fotografia;

}