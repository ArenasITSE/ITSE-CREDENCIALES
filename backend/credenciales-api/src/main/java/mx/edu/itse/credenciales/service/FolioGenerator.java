package mx.edu.itse.credenciales.service;

import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.repository.CredencialRepository;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class FolioGenerator {

    private final CredencialRepository credencialRepository;

    public String generar() {

        Credencial ultima =
                credencialRepository.findTopByOrderByIdDesc();

        int consecutivo = 1;

        if (ultima != null) {

            try {

                String ultimoFolio =
                        ultima.getFolio();

                String numero =
                        ultimoFolio.substring(
                                ultimoFolio.lastIndexOf("-") + 1
                        );

                consecutivo =
                        Integer.parseInt(numero) + 1;

            } catch (Exception e) {

                consecutivo = 1;

            }

        }

        return String.format(

                "ITSE-%d-%06d",

                Year.now().getValue(),

                consecutivo

        );

    }

}