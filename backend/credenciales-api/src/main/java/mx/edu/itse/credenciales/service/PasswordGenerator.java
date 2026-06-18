package mx.edu.itse.credenciales.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordGenerator {

    private static final String MAYUSCULAS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String MINUSCULAS =
            "abcdefghijklmnopqrstuvwxyz";

    private static final String NUMEROS =
            "0123456789";

    private static final String ESPECIALES =
            "@#$%&*?!";

    private static final String TODOS =

            MAYUSCULAS +

            MINUSCULAS +

            NUMEROS +

            ESPECIALES;

    private final SecureRandom random = new SecureRandom();

    /**
     * Genera una contraseña segura de 10 caracteres
     */
    public String generar() {

        StringBuilder password = new StringBuilder();

        // Garantizar al menos un carácter de cada tipo

        password.append(
                MAYUSCULAS.charAt(
                        random.nextInt(MAYUSCULAS.length())
                )
        );

        password.append(
                MINUSCULAS.charAt(
                        random.nextInt(MINUSCULAS.length())
                )
        );

        password.append(
                NUMEROS.charAt(
                        random.nextInt(NUMEROS.length())
                )
        );

        password.append(
                ESPECIALES.charAt(
                        random.nextInt(ESPECIALES.length())
                )
        );

        // Completar hasta 10 caracteres

        while (password.length() < 10) {

            password.append(

                    TODOS.charAt(

                            random.nextInt(TODOS.length())

                    )

            );

        }

        // Mezclar caracteres

        return mezclar(password.toString());

    }

    /**
     * Mezcla aleatoriamente los caracteres
     */
    private String mezclar(String texto) {

        char[] arreglo = texto.toCharArray();

        for (int i = 0; i < arreglo.length; i++) {

            int j = random.nextInt(arreglo.length);

            char aux = arreglo[i];

            arreglo[i] = arreglo[j];

            arreglo[j] = aux;

        }

        return new String(arreglo);

    }

}