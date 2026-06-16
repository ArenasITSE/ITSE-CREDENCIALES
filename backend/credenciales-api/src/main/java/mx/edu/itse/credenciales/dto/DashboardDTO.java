package mx.edu.itse.credenciales.dto;

import lombok.Builder;
import java.util.List;
import lombok.Data;

@Data
@Builder
public class DashboardDTO {

    private Long totalAlumnos;

    private Long totalCarreras;

    private Long totalCredenciales;

    private Long credencialesValidadas;

    private Long credencialesCanceladas;

    private List<CarreraDashboardDTO> alumnosPorCarrera;

    private List<SemestreDashboardDTO> alumnosPorSemestre;

private List<EstadoCredencialDashboardDTO> credencialesPorEstado;

private List<UltimaCredencialDTO> ultimasCredenciales;

private List<UltimoAlumnoDTO> ultimosAlumnos;
}