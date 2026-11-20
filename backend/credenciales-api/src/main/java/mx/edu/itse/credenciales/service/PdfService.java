package mx.edu.itse.credenciales.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import mx.edu.itse.credenciales.entity.Credencial;
import mx.edu.itse.credenciales.repository.CredencialRepository;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Element;
import mx.edu.itse.credenciales.service.HistorialCredencialService;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final CredencialRepository credencialRepository;
    private final HistorialCredencialService historialService;

    public String generarPdf(Long credencialId) throws Exception {

        Credencial credencial =
                credencialRepository.findById(credencialId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Credencial no encontrada"
                                ));

        Files.createDirectories(
                Paths.get("uploads/credenciales")
        );

        String rutaPdf =
                "uploads/credenciales/"
                        + credencial.getFolio()
                        + ".pdf";

        Document document =
                new Document(
                        new Rectangle(
                                420,
                                280
                        ),
                        0,
                        0,
                        0,
                        0
                );

        PdfWriter writer =
                PdfWriter.getInstance(
                        document,
                        new FileOutputStream(
                                rutaPdf
                        )
                );

        document.open();

        PdfContentByte canvas =
                writer.getDirectContent();

        // ===========================
        // FONDO
        // ===========================

        Image fondo =
                Image.getInstance(

                        new ClassPathResource(
                                "plantillas/credencial_frente1.png"
                        )
                                .getInputStream()
                                .readAllBytes()

                );

        fondo.scaleAbsolute(
                420,
                280
        );

        fondo.setAbsolutePosition(
                0,
                0
        );

        document.add(fondo);

        // ===========================
        // FOTOGRAFÍA
        // ===========================

        if (credencial.getAlumno().getFotografia() != null) {

            try {

                File archivoFoto =
                        new File(
                                credencial.getAlumno()
                                        .getFotografia()
                                        .getRuta()
                        );

                if (archivoFoto.exists()) {

                    Image foto =
                            Image.getInstance(
                                    archivoFoto.getAbsolutePath()
                            );

                    foto.scaleAbsolute(
                            90,
                            118
                    );

                    foto.setAbsolutePosition(
                            22,
                            88
                    );

                    document.add(
                            foto
                    );

                }

            } catch (Exception e) {

                System.out.println(
                        "No fue posible cargar la fotografía."
                );

            }

        }

        // ===========================
        // FUENTES
        // ===========================

        BaseFont helvetica =
                BaseFont.createFont(
                        BaseFont.HELVETICA,
                        BaseFont.CP1252,
                        BaseFont.NOT_EMBEDDED
                );

        Font nombreFont =
                new Font(
                        helvetica,
                        8,
                        Font.NORMAL,
                        new BaseColor(
                                35,
                                35,
                                35
                        )
                );

        Font datosFont =
                new Font(
                        helvetica,
                        9,
                        Font.NORMAL,
                        BaseColor.BLACK
                );

        Font estadoFont =
                new Font(
                        helvetica,
                        9,
                        Font.BOLD,
                        new BaseColor(
                                120,
                                0,
                                30
                        )
                );

        Font pieFont =
                new Font(
                        helvetica,
                        6,
                        Font.NORMAL,
                        BaseColor.DARK_GRAY
                );

        // ===========================
        // NOMBRE DEL ALUMNO
        // ===========================

        escribirTexto(

                canvas,

                helvetica,

                credencial.getAlumno()
                        .getNombreCompleto(),

                172,
                188,
                165,

                Font.NORMAL,

                new BaseColor(
                        35,
                        35,
                        35
                )

        );

        // ===========================
        // CARRERA
        // ===========================

        escribirCarrera(

                canvas,

                helvetica,

                credencial.getAlumno()
                        .getCarrera()
                        .getNombre(),

                173,
                175,
                170

        );

        // ===========================
        // MATRÍCULA
        // ===========================

        ColumnText.showTextAligned(

                canvas,

                Element.ALIGN_LEFT,

                new Phrase(

                        credencial.getAlumno()
                                .getMatricula(),

                        datosFont

                ),

                183,
                140,
                0

        );

        // ===========================
        // SEMESTRE
        // ===========================

        ColumnText.showTextAligned(

                canvas,

                Element.ALIGN_LEFT,

                new Phrase(
                        String.valueOf(
                                credencial.getAlumno().getSemestre()
                        )
                ),

                180,
                116,

                0

        );

        // ===========================
        // FOLIO
        // ===========================

        ColumnText.showTextAligned(

                canvas,

                Element.ALIGN_LEFT,

                new Phrase(

                        credencial.getFolio(),

                        datosFont

                ),

                170,
                94,
                0

        );

        // ===========================
        // NSS
        // ===========================

        ColumnText.showTextAligned(

                canvas,

                Element.ALIGN_LEFT,

                new Phrase(

                        credencial.getAlumno()
                                .getNss(),

                        datosFont

                ),

                170,
                73,
                0

        );

        // ===========================
        // ESTADO
        // ===========================

        ColumnText.showTextAligned(

                canvas,

                Element.ALIGN_LEFT,

                new Phrase(

                        credencial.getEstado(),

                        estadoFont

                ),

                255,
                117,
                0

        );

        // ===========================
        // QR
        // ===========================

        if (credencial.getQrPath() != null) {

            try {

                File qrFile =
                        new File(
                                credencial.getQrPath()
                        );

                if (qrFile.exists()) {

                    Image qr =
                            Image.getInstance(
                                    qrFile.getAbsolutePath()
                            );

                    qr.scaleAbsolute(
                            60,
                            60
                    );

                    qr.setAbsolutePosition(
                            326,
                            75
                    );

                    document.add(
                            qr
                    );

                }

            } catch (Exception e) {

                System.out.println(
                        "No fue posible cargar el QR."
                );

            }

        }

        // ===========================
        // CERRAR DOCUMENTO
        // ===========================

        document.close();

        historialService.registrarEvento(

                credencial,

                "PDF generado",

                "ADMIN"

        );

        return rutaPdf;

    }

    // ==========================================================
    // MÉTODOS AUXILIARES
    // ==========================================================

    /**
     * Ajusta automáticamente el tamaño de la fuente para que el texto
     * no se salga del espacio disponible.
     */
    private Font ajustarFuente(
            BaseFont baseFont,
            String texto,
            float anchoMaximo,
            int estilo,
            BaseColor color
    ) {

        float size = 10;

        while (size >= 7) {

            float width =
                    baseFont.getWidthPoint(
                            texto,
                            size
                    );

            if (width <= anchoMaximo) {

                break;

            }

            size--;

        }

        return new Font(
                baseFont,
                size,
                estilo,
                color
        );

    }

    /**
     * Dibuja texto adaptando automáticamente el tamaño.
     */
    private void escribirTexto(

            PdfContentByte canvas,

            BaseFont baseFont,

            String texto,

            float x,

            float y,

            float anchoMaximo,

            int estilo,

            BaseColor color

    ) {

        Font fuente =
                ajustarFuente(
                        baseFont,
                        texto,
                        anchoMaximo,
                        estilo,
                        color
                );

        ColumnText.showTextAligned(

                canvas,

                Element.ALIGN_LEFT,

                new Phrase(
                        texto,
                        fuente
                ),

                x,

                y,

                0

        );

    }

    /**
     * Divide automáticamente una carrera muy larga en dos líneas.
     */
    private void escribirCarrera(

            PdfContentByte canvas,

            BaseFont baseFont,

            String carrera,

            float x,

            float y,

            float ancho

    ) {

        Font fuente =
                new Font(
                        baseFont,
                        8,
                        Font.NORMAL,
                        BaseColor.BLACK
                );

        ColumnText ct =
                new ColumnText(canvas);

        ct.setSimpleColumn(

                new Phrase(
                        carrera,
                        fuente
                ),

                x,

                y - 18,

                x + ancho,

                y,

                10,

                Element.ALIGN_LEFT

        );

        try {

            ct.go();

        } catch (DocumentException e) {

            e.printStackTrace();

        }

    }

    public String regenerarPdf(Long credencialId) throws Exception {

        Credencial credencial =
                credencialRepository.findById(credencialId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Credencial no encontrada"
                                ));

        String rutaPdf =
                "uploads/credenciales/"
                        + credencial.getFolio()
                        + ".pdf";

        Files.deleteIfExists(
                Paths.get(rutaPdf)
        );

        return generarPdf(credencialId);

    }

}