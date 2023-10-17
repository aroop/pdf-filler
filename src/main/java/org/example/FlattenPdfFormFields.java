package org.example;

// Path: FlattenPdfFormFields.java

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import java.util.Map;

public class FlattenPdfFormFields {
    public static void main(String[] args) {
        try {
            // Initialize PdfReader, PdfWriter, and PdfDocument
            PdfReader pdfReader = new PdfReader("705.pdf");
            PdfWriter pdfWriter = new PdfWriter("flatten-705.pdf");
            PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);

            // Retrieve and fill AcroForm fields
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDocument, true);
            Map<String, PdfFormField> fields = acroForm.getAllFormFields();
            for (Map.Entry<String, PdfFormField> entry : fields.entrySet()) {
                String key = entry.getKey();
                PdfFormField field = entry.getValue();
                field.setValue("Test Data for " + key);
            }

            // Flatten the form
            acroForm.flattenFields();

            // Save and close the document
            pdfDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
