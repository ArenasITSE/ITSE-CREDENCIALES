package mx.edu.itse.credenciales.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class QrService {

    public String generarQR(
            String contenido,
            String nombreArchivo
    ) throws Exception {

        QRCodeWriter qrCodeWriter =
                new QRCodeWriter();

        BitMatrix bitMatrix =
                qrCodeWriter.encode(
                        contenido,
                        BarcodeFormat.QR_CODE,
                        400,
                        400
                );

        String carpeta = "uploads/qr";

        Files.createDirectories(
                Paths.get(carpeta)
        );

        String ruta =
                carpeta +
                "/" +
                nombreArchivo +
                ".png";

        Path path =
                Paths.get(ruta);

        MatrixToImageWriter.writeToPath(
                bitMatrix,
                "PNG",
                path
        );

        return ruta;
    }
}