package com.example.demo;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.FileNotFoundException;

public class PdfGenerator {
    public void generatePDF(String filePath) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Hello, this is a PDF generated from JavaFX!"));

        document.close();
        PdfGenerator pdfGenerator = new PdfGenerator();
        pdfGenerator.generatePDF("output.pdf");
    }
    public static void main(String[] args) throws FileNotFoundException {
    }
}
