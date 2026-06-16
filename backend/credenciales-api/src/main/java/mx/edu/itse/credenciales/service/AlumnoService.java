package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.AlumnoActualizarDTO;
import mx.edu.itse.credenciales.dto.AlumnoDTO;
import mx.edu.itse.credenciales.dto.AlumnoResponseDTO;
import mx.edu.itse.credenciales.entity.Alumno;
import mx.edu.itse.credenciales.entity.Carrera;
import mx.edu.itse.credenciales.entity.Rol;
import mx.edu.itse.credenciales.entity.Usuario;
import mx.edu.itse.credenciales.repository.AlumnoRepository;
import mx.edu.itse.credenciales.repository.CarreraRepository;
import mx.edu.itse.credenciales.repository.RolRepository;
import mx.edu.itse.credenciales.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import mx.edu.itse.credenciales.dto.AlumnoActualizarDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CarreraRepository carreraRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Alumno> obtenerTodos() {

        return alumnoRepository.findAll();

    }

    public Alumno obtenerPorId(Long id) {

        return alumnoRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Alumno no encontrado"
                        ));
    }

    public AlumnoResponseDTO registrarAlumno(
            AlumnoDTO dto
    ) {

        if (usuarioRepository.existsByUsername(
                dto.getMatricula()
        )) {

            throw new RuntimeException(
                    "La matrícula ya existe"
            );
        }

        Carrera carrera =
                carreraRepository.findById(
                        dto.getCarreraId()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Carrera no encontrada"
                        ));

        Rol rolAlumno =
                rolRepository.findByNombre(
                        "ALUMNO"
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Rol ALUMNO no encontrado"
                        ));

        String username =
                dto.getMatricula();

        String passwordTemporal =
                "ITSE@" + dto.getMatricula();

        Usuario usuario =
                Usuario.builder()
                        .username(username)
                        .password(
                                passwordEncoder.encode(
                                        passwordTemporal
                                )
                        )
                        .rol(rolAlumno)
                        .activo(true)
                        .build();

        usuario =
                usuarioRepository.save(
                        usuario
                );

        Alumno alumno =
                Alumno.builder()
                        .nombreCompleto(
                                dto.getNombreCompleto()
                        )
                        .matricula(
                                dto.getMatricula()
                        )
                        .semestre(
                                dto.getSemestre()
                        )
                        .activo(true)
                        .usuario(usuario)
                        .carrera(carrera)
                        .build();

        alumnoRepository.save(
                alumno
        );

        return AlumnoResponseDTO.builder()
                .mensaje(
                        "Alumno registrado correctamente"
                )
                .usuario(
                        username
                )
                .passwordTemporal(
                        passwordTemporal
                )
                .build();
    }

    public void eliminar(Long id) {

        Alumno alumno =
                obtenerPorId(id);

        alumno.setActivo(false);

        alumnoRepository.save(
                alumno
        );
    }


    public String restablecerPassword(Long alumnoId) {

    Alumno alumno = alumnoRepository.findById(alumnoId)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Alumno no encontrado"
                    ));

    Usuario usuario = alumno.getUsuario();

    String passwordTemporal =
            "ITSE@" + alumno.getMatricula();

    usuario.setPassword(
            passwordEncoder.encode(
                    passwordTemporal
            )
    );

    usuarioRepository.save(usuario);

    return passwordTemporal;

}

public Alumno actualizarAlumno(
        Long id,
        AlumnoActualizarDTO dto
) {

    Alumno alumno = alumnoRepository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Alumno no encontrado"
                    ));

    Carrera carrera = carreraRepository.findById(
            dto.getCarreraId()
    ).orElseThrow(() ->
            new RuntimeException(
                    "Carrera no encontrada"
            ));

    alumno.setNombreCompleto(
            dto.getNombreCompleto()
    );

    alumno.setSemestre(
            dto.getSemestre()
    );

    alumno.setCarrera(
            carrera
    );

    return alumnoRepository.save(
            alumno
    );

}

    
}