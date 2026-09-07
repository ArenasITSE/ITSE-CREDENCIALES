package mx.edu.itse.credenciales.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import mx.edu.itse.credenciales.dto.AlumnoActualizarDTO;
import mx.edu.itse.credenciales.dto.AlumnoDTO;
import mx.edu.itse.credenciales.dto.AlumnoResponseDTO;
import mx.edu.itse.credenciales.dto.RegistrarAlumnoRequest;
import mx.edu.itse.credenciales.dto.RegistrarAlumnoResponse;

import mx.edu.itse.credenciales.entity.Alumno;
import mx.edu.itse.credenciales.entity.Carrera;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.entity.Rol;
import mx.edu.itse.credenciales.entity.Usuario;

import mx.edu.itse.credenciales.repository.AlumnoRepository;
import mx.edu.itse.credenciales.repository.CarreraRepository;
import mx.edu.itse.credenciales.repository.RolRepository;
import mx.edu.itse.credenciales.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    //=========================================
    // REPOSITORIOS
    //=========================================

    private final AlumnoRepository alumnoRepository;

    private final UsuarioRepository usuarioRepository;

    private final CarreraRepository carreraRepository;

    private final RolRepository rolRepository;

    //=========================================
    // SERVICES
    //=========================================

    private final CredencialService credencialService;

    private final FotografiaService fotografiaService;

    //=========================================
    // UTILIDADES
    //=========================================

    private final PasswordEncoder passwordEncoder;

    private final PasswordGenerator passwordGenerator;

    //=========================================
    // OBTENER TODOS
    //=========================================

    public List<Alumno> obtenerTodos() {

        return alumnoRepository.findAll();

    }

    //=========================================
    // OBTENER POR ID
    //=========================================

    public Alumno obtenerPorId(Long id) {

        return alumnoRepository.findById(id)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Alumno no encontrado"
                        )

                );

    }

    //=========================================
    // REGISTRAR ALUMNO
    //=========================================

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
                )

                .orElseThrow(() ->

                        new RuntimeException(
                                "Carrera no encontrada"
                        )

                );

        Rol rolAlumno =

                rolRepository.findByNombre(
                        "ALUMNO"
                )

                .orElseThrow(() ->

                        new RuntimeException(
                                "Rol ALUMNO no encontrado"
                        )

                );

        String username =

                dto.getMatricula();

        String passwordTemporal =

                passwordGenerator.generar();

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

                        .nss(
                                dto.getNss()
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

    //=========================================
    // ELIMINAR ALUMNO
    //=========================================

    public void eliminar(Long id) {

        Alumno alumno =

                obtenerPorId(id);

        alumno.setActivo(false);

        alumnoRepository.save(
                alumno
        );

    }

    //=========================================
    // RESTABLECER PASSWORD
    //=========================================

    public String restablecerPassword(Long alumnoId) {

        Alumno alumno =

                alumnoRepository.findById(alumnoId)

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Alumno no encontrado"
                                )

                        );

        Usuario usuario =

                alumno.getUsuario();

        String passwordTemporal =

                passwordGenerator.generar();

        usuario.setPassword(

                passwordEncoder.encode(
                        passwordTemporal
                )

        );

        usuarioRepository.save(
                usuario
        );

        return passwordTemporal;

    }

    //=========================================
    // ACTUALIZAR ALUMNO
    //=========================================

    @Transactional
    public Alumno actualizarAlumno(

            Long id,

            AlumnoActualizarDTO dto

    ) {

        Alumno alumno =

                alumnoRepository.findById(id)

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Alumno no encontrado"
                                )

                        );

        //=========================================
        // VALIDAR Y ACTUALIZAR MATRÍCULA
        //=========================================

        if (!alumno.getMatricula().equals(
                dto.getMatricula()
        )) {

            // Verificar que la nueva matrícula
            // no pertenezca a otro alumno

            if (alumnoRepository.existsByMatricula(
                    dto.getMatricula()
            )) {

                throw new RuntimeException(
                        "La matrícula ya existe"
                );

            }

            // Verificar también que no exista
            // como username de otro usuario

            if (usuarioRepository.existsByUsername(
                    dto.getMatricula()
            )) {

                throw new RuntimeException(
                        "La matrícula ya está asociada a otro usuario"
                );

            }

            // Actualizar matrícula del alumno

            alumno.setMatricula(
                    dto.getMatricula()
            );

            // Actualizar username del usuario asociado

            if (alumno.getUsuario() != null) {

                alumno.getUsuario().setUsername(
                        dto.getMatricula()
                );

                usuarioRepository.save(
                        alumno.getUsuario()
                );

            }

        }

        //=========================================
        // ACTUALIZAR CARRERA
        //=========================================

        Carrera carrera =

                carreraRepository.findById(
                        dto.getCarreraId()
                )

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Carrera no encontrada"
                                )

                        );

        //=========================================
        // ACTUALIZAR DATOS
        //=========================================

        alumno.setNombreCompleto(
                dto.getNombreCompleto()
        );

        alumno.setSemestre(
                dto.getSemestre()
        );

        alumno.setNss(
                dto.getNss()
        );

        alumno.setCarrera(
                carrera
        );

        return alumnoRepository.save(
                alumno
        );

    }

    //=========================================
    // REGISTRO COMPLETO
    //=========================================

    @Transactional
    public RegistrarAlumnoResponse registrarCompleto(
            RegistrarAlumnoRequest dto
    ) throws Exception {

        //=====================================
        // VALIDAR MATRÍCULA
        //=====================================

        if (usuarioRepository.existsByUsername(
                dto.getMatricula()
        )) {

            throw new RuntimeException(
                    "La matrícula ya existe"
            );

        }

        //=====================================
        // CARRERA
        //=====================================

        Carrera carrera =

                carreraRepository.findById(
                        dto.getCarreraId()
                )

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Carrera no encontrada"
                                )

                        );

        //=====================================
        // ROL
        //=====================================

        Rol rol =

                rolRepository.findByNombre(
                        "ALUMNO"
                )

                        .orElseThrow(() ->

                                new RuntimeException(
                                        "Rol ALUMNO no encontrado"
                                )

                        );

        //=====================================
        // PASSWORD
        //=====================================

        String passwordTemporal =

                passwordGenerator.generar();

        //=====================================
        // USUARIO
        //=====================================

        Usuario usuario =

                Usuario.builder()

                        .username(
                                dto.getMatricula()
                        )

                        .password(

                                passwordEncoder.encode(
                                        passwordTemporal
                                )

                        )

                        .rol(rol)

                        .activo(true)

                        .build();

        usuario =

                usuarioRepository.save(
                        usuario
                );

        //=====================================
        // ALUMNO
        //=====================================

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

                        .nss(
                                dto.getNss()
                        )

                        .activo(true)

                        .usuario(usuario)

                        .carrera(carrera)

                        .build();

        alumno =

                alumnoRepository.save(
                        alumno
                );

        //=====================================
        // FOTOGRAFÍA
        //=====================================

        if (dto.getFoto() != null &&
                !dto.getFoto().isEmpty()) {

            fotografiaService.subirFoto(

                    alumno.getId(),

                    dto.getFoto()

            );

            alumno = obtenerPorId(
                    alumno.getId()
            );

        }

        //=====================================
        // CREDENCIAL
        //=====================================

        Credencial credencial =

                credencialService.generarCredencial(
                        alumno
                );

        //=====================================
        // RESPUESTA
        //=====================================

        return RegistrarAlumnoResponse.builder()

                .id(
                        alumno.getId()
                )

                .usuario(
                        usuario.getUsername()
                )

                .passwordTemporal(
                        passwordTemporal
                )

                .folio(
                        credencial.getFolio()
                )

                .mensaje(
                        "Alumno registrado correctamente"
                )

                .build();

    }

    //=========================================
    // ELIMINAR ALUMNO COMPLETO
    //=========================================

    @Transactional
    public void eliminarCompleto(
            Long alumnoId
    ) throws Exception {

        Alumno alumno =

                obtenerPorId(alumnoId);

        //=========================
        // ELIMINAR CREDENCIAL
        //=========================

        if (alumno.getId() != null) {

            credencialService

                    .obtenerTodas()

                    .stream()

                    .filter(c ->

                            c.getAlumno()
                                    .getId()
                                    .equals(alumnoId)

                    )

                    .findFirst()

                    .ifPresent(c -> {

                        credencialService
                                .eliminarCredencial(
                                        c.getId()
                                );

                    });

        }

        //=========================
        // ELIMINAR FOTO
        //=========================

        if (alumno.getFotografia() != null) {

            fotografiaService.eliminarFoto(

                    alumno.getFotografia().getId()

            );

        }

        //=========================
        // ELIMINAR USUARIO
        //=========================

        if (alumno.getUsuario() != null) {

            usuarioRepository.delete(
                    alumno.getUsuario()
            );

        }

        //=========================
        // ELIMINAR ALUMNO
        //=========================

        alumnoRepository.delete(
                alumno
        );

    }

}