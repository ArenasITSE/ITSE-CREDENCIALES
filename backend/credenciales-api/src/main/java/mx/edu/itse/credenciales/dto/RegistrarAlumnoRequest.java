package mx.edu.itse.credenciales.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegistrarAlumnoRequest {

    private String usuario;

    private String nombreCompleto;

    private String matricula;

    private Long carreraId;

    private Integer semestre;

    private MultipartFile foto;

}