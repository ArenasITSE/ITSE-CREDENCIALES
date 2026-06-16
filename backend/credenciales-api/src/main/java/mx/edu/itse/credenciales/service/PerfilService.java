package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.dto.PerfilResponse;
import mx.edu.itse.credenciales.entity.Alumno;
import mx.edu.itse.credenciales.repository.AlumnoRepository;
import org.springframework.stereotype.Service;
import mx.edu.itse.credenciales.dto.CambiarPasswordDTO;
import mx.edu.itse.credenciales.entity.Usuario;
import mx.edu.itse.credenciales.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@RequiredArgsConstructor
public class PerfilService {

    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;

private final PasswordEncoder passwordEncoder;

    public PerfilResponse obtenerPerfil(
            String username
    ) {

        Alumno alumno =
                alumnoRepository
                        .findByUsuarioUsername(
                                username
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Alumno no encontrado"
                                ));

        return PerfilResponse.builder()
                .matricula(
                        alumno.getMatricula()
                )
                .nombreCompleto(
                        alumno.getNombreCompleto()
                )
                .semestre(
                        alumno.getSemestre()
                )
                .carrera(
                        alumno.getCarrera()
                                .getNombre()
                )
                .fotografia(
                        alumno.getFotografia() != null
                                ? alumno.getFotografia().getRuta()
                                : null
                )
                .build();
    }

 public void cambiarPassword(

        String username,

        CambiarPasswordDTO dto

) {

    Usuario usuario =

            usuarioRepository
                    .findByUsername(username)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Usuario no encontrado"
                            ));

    // Verificar contraseña actual

    if (!passwordEncoder.matches(

            dto.getPasswordActual(),

            usuario.getPassword()

    )) {

        throw new RuntimeException(
                "La contraseña actual es incorrecta"
        );

    }

    // Verificar que coincidan

    if (!dto.getPasswordNueva().equals(

            dto.getConfirmarPassword()

    )) {

        throw new RuntimeException(
                "Las contraseñas no coinciden"
        );

    }

    // Longitud mínima

    if (dto.getPasswordNueva().length() < 8) {

        throw new RuntimeException(
                "La contraseña debe tener al menos 8 caracteres"
        );

    }

    // No permitir la misma contraseña

    if (passwordEncoder.matches(

            dto.getPasswordNueva(),

            usuario.getPassword()

    )) {

        throw new RuntimeException(
                "La nueva contraseña debe ser diferente a la actual"
        );

    }

    usuario.setPassword(

            passwordEncoder.encode(

                    dto.getPasswordNueva()

            )

    );

    usuarioRepository.save(usuario);

}
    
}