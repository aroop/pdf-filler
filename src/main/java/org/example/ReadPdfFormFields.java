package org.example;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import java.util.Map;

public class ReadPdfFormFields {
    public static void main(String[] args) {
        try {
            // Initialize PdfReader and PdfDocument
            PdfReader pdfReader = new PdfReader("4905PIT.pdf");
            PdfDocument pdfDocument = new PdfDocument(pdfReader);

            // Retrieve AcroForm fields
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDocument, false);
            Map<String, PdfFormField> fields = acroForm.getAllFormFields();

            // Iterate through fields and print their keys and values
            for (Map.Entry<String, PdfFormField> entry : fields.entrySet()) {
                String key = entry.getKey();
                PdfFormField field = entry.getValue();
                System.out.println("Field name: " + key);
                System.out.println("Field value: " + field.getValueAsString());
            }

            // Close resources
            pdfDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
