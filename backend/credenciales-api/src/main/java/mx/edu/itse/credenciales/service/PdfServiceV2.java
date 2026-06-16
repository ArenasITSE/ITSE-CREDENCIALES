package mx.edu.itse.credenciales.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.repository.CredencialRepository;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class PdfServiceV2 {

    private final CredencialRepository credencialRepository;

    public String generarPdf(Long credencialId) throws Exception {

        Credencial credencial =
                credencialRepository.findById(credencialId)
                        .orElseThrow(() ->
                                new RuntimeException("Credencial no encontrada"));

        Files.createDirectories(
                Paths.get("uploads/credenciales")
        );

        String rutaPdf =
                "uploads/credenciales/"
                        + credencial.getFolio()
                        + ".pdf";

        Document document =
                new Document(new Rectangle(250,420));

        PdfWriter writer =
                PdfWriter.getInstance(
                        document,
                        new FileOutputStream(rutaPdf)
                );

        document.open();

        PdfContentByte canvas =
                writer.getDirectContent();

                Image fondo =
        Image.getInstance(
                "src/main/resources/plantillas/credencial_frente.png"
        );

fondo.scaleAbsolute(
        250,
        420
);

fondo.setAbsolutePosition(
        0,
        0
);

document.add(fondo);

if(credencial.getAlumno().getFotografia()!=null){

    try{

        Image foto =
                Image.getInstance(
                        credencial.getAlumno()
                                .getFotografia()
                                .getRuta()
                );

        foto.scaleAbsolute(
                90,
                90
        );

        foto.setAbsolutePosition(
                18,
                270
        );

        document.add(foto);

    }catch(Exception e){

        System.out.println("No se cargó la fotografía");

    }

}

BaseFont baseFont =
        BaseFont.createFont(
                BaseFont.HELVETICA,
                BaseFont.CP1252,
                false
        );

Font nombre =
        new Font(baseFont,13,Font.BOLD,
                new BaseColor(30,30,30));

Font datos =
        new Font(baseFont,9,Font.NORMAL,
                BaseColor.BLACK);

Font titulo =
        new Font(baseFont,8,Font.BOLD,
                new BaseColor(120,0,30));

                ColumnText.showTextAligned(

        canvas,

        Element.ALIGN_LEFT,

        new Phrase(
                credencial.getAlumno()
                        .getNombreCompleto(),
                nombre
        ),

        120,

        325,

        0

);

ColumnText.showTextAligned(

        canvas,

        Element.ALIGN_LEFT,

        new Phrase(
                credencial.getAlumno()
                        .getCarrera()
                        .getNombre(),
                datos
        ),

        120,

        292,

        0

);

ColumnText.showTextAligned(

        canvas,

        Element.ALIGN_LEFT,

        new Phrase(
                "Matrícula: "
                        + credencial.getAlumno().getMatricula(),
                datos
        ),

        120,

        270,

        0

);

ColumnText.showTextAligned(

        canvas,

        Element.ALIGN_LEFT,

        new Phrase(
                "Semestre: "
                        + credencial.getAlumno().getSemestre(),
                datos
        ),

        120,

        252,

        0

);

// =============================
// FOLIO
// =============================

ColumnText.showTextAligned(

        canvas,

        Element.ALIGN_LEFT,

        new Phrase(
                "Folio: " +
                        credencial.getFolio(),
                datos
        ),

        120,

        234,

        0

);

// =============================
// ESTADO
// =============================

ColumnText.showTextAligned(

        canvas,

        Element.ALIGN_LEFT,

        new Phrase(
                "Estado: " +
                        credencial.getEstado(),
                titulo
        ),

        120,

        216,

        0

);

// =============================
// QR
// =============================

if (credencial.getQrPath() != null) {

    try {

        Image qr =
                Image.getInstance(
                        credencial.getQrPath()
                );

        qr.scaleAbsolute(
                90,
                90
        );

        qr.setAbsolutePosition(
                80,
                85
        );

        document.add(qr);

    } catch (Exception e) {

        System.out.println(
                "No se pudo cargar el QR"
        );

    }

}



// =============================
// NUMERO BAJO CODIGO
// =============================

ColumnText.showTextAligned(

        canvas,

        Element.ALIGN_CENTER,

        new Phrase(
                credencial.getAlumno()
                        .getMatricula(),
                datos
        ),

        125,

        32,

        0

);

// =============================
// PIE
// =============================

ColumnText.showTextAligned(

        canvas,

        Element.ALIGN_CENTER,

        new Phrase(
                "INSTITUTO TECNOLÓGICO SUPERIOR DE ESCÁRCEGA",
                new Font(
                        baseFont,
                        6,
                        Font.BOLD,
                        BaseColor.GRAY
                )
        ),

        125,

        15,

        0

);

document.close();

return rutaPdf;

    }

}
