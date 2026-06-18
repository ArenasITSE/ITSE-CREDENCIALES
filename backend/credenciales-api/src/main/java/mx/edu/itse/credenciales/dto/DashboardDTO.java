package mx.edu.itse.credenciales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    // ==========================================
    // TARJETAS PRINCIPALES
    // ==========================================

    private Long totalAlumnos;

    private Long credencialesActivas;

    private Long credencialesCanceladas;

    private Long totalCarreras;

    // ==========================================
    // RESUMEN
    // ==========================================

    private String ultimoAlumno;

    private String ultimaCredencial;

    private String ultimaActualizacion;

    // ==========================================
    // GRÁFICAS
    // ==========================================

    private List<CarreraDashboardDTO> alumnosPorCarrera;

    private List<SemestreDashboardDTO> alumnosPorSemestre;

    private List<EstadoCredencialDashboardDTO> credencialesPorEstado;

    // ==========================================
    // ACTIVIDAD
    // ==========================================

    private List<UltimoAlumnoDTO> ultimosAlumnos;

    private List<UltimaCredencialDTO> ultimasCredenciales;

}