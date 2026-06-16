package mx.edu.itse.credenciales.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Usuario;
import mx.edu.itse.credenciales.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(
            @PathVariable Long id
    ) {
        return usuarioService.obtenerPorId(id);
    }

    @PostMapping
    public Usuario crear(
            @RequestBody Usuario usuario
    ) {
        return usuarioService.crearUsuario(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario
    ) {
        return usuarioService.actualizarUsuario(
                id,
                usuario
        );
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id
    ) {
        usuarioService.eliminarUsuario(id);
    }
}