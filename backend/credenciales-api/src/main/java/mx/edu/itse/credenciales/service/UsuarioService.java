package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Usuario;
import mx.edu.itse.credenciales.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado"));
    }

    public Usuario crearUsuario(Usuario usuario) {

        usuario.setPassword(
                passwordEncoder.encode(
                        usuario.getPassword()
                )
        );

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(
            Long id,
            Usuario datos
    ) {

        Usuario usuario = obtenerPorId(id);

        usuario.setUsername(
                datos.getUsername()
        );

        if (datos.getPassword() != null &&
                !datos.getPassword().isBlank()) {

            usuario.setPassword(
                    passwordEncoder.encode(
                            datos.getPassword()
                    )
            );
        }

        usuario.setActivo(
                datos.getActivo()
        );

        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}