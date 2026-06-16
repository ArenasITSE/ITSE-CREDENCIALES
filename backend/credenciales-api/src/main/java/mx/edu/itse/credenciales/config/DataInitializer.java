package mx.edu.itse.credenciales.config;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Rol;
import mx.edu.itse.credenciales.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) {

        crearRolSiNoExiste("SUPERADMIN");
        crearRolSiNoExiste("ADMINISTRADOR");
        crearRolSiNoExiste("FOTOGRAFO");
        crearRolSiNoExiste("ALUMNO");

    }

    private void crearRolSiNoExiste(String nombreRol) {

        if (rolRepository.findByNombre(nombreRol).isEmpty()) {

            Rol rol = Rol.builder()
                    .nombre(nombreRol)
                    .build();

            rolRepository.save(rol);
        }
    }
}