package org.example;

// Path: FillPdfFormFields.java

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import java.util.Map;

public class FillPdfFormFields {
    public static void main(String[] args) {
        try {
            // Initialize PdfReader and PdfDocument
            PdfReader pdfReader = new PdfReader("705.pdf");
            PdfWriter pdfWriter = new PdfWriter("filled-output.pdf");
            PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);

            // Retrieve AcroForm fields
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDocument, true);
            Map<String, PdfFormField> fields = acroForm.getAllFormFields();

            // Iterate through fields and fill them with test data
            for (Map.Entry<String, PdfFormField> entry : fields.entrySet()) {
                String key = entry.getKey();
                PdfFormField field = entry.getValue();
                field.setValue("Test Data for " + key);
            }

            // Save and close the document
            pdfDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
