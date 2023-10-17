package org.example;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;

public class HelloWorldPDF
{
    public static void main(String[] args) {
        // Step 1: Create a PdfWriter
        String dest = "HelloWorld.pdf";
        PdfWriter writer = null;
        try {
            writer = new PdfWriter(dest);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Step 2: Create a PdfDocument
        PdfDocument pdf = new PdfDocument(writer);

        // Step 3: Create a Document
        Document document = new Document(pdf);

        // Step 4: Add "Hello, World!" text
        document.add(new Paragraph("Hello, World!"));

        // Step 5: Close the document
        document.close();

        System.out.println("PDF created successfully at " + dest);
    }
}
