package org.example;

// Path: ReadPdfFormFields.java

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import java.util.Map;

public class ReadPdfFormFields {
    public static void main(String[] args) {
        try {
            // Initialize PdfReader and PdfDocument
            PdfReader pdfReader = new PdfReader("705.pdf");
            PdfDocument pdfDocument = new PdfDocument(pdfReader);

            // Retrieve AcroForm fields
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDocument, false);
            Map<String, PdfFormField> fields = acroForm.getFormFields();

            // Iterate through fields and print their attributes
            for (Map.Entry<String, PdfFormField> entry : fields.entrySet()) {
                String key = entry.getKey();
                PdfFormField field = entry.getValue();

                System.out.println("Field name: " + key);
                System.out.println("Readonly: " + field.isReadOnly());
                System.out.println("AlternativeName: " + field.getAlternativeName());
                System.out.println("MappingName: " + field.getMappingName());
                System.out.println("Multiline: " + field.isMultiline());
                System.out.println("Password: " + field.isPassword());
                System.out.println("Field value: " + field.getValueAsString());
                System.out.println("PartialFieldName: " + field.getFieldName());
                System.out.println("Is required: " + field.isRequired());
                System.out.println("Default value: " + field.getDefaultValue());
                System.out.println("Class name: " + field.getClass().getName());

                // Add more attributes if needed
                System.out.println("---------------------------");
            }

            // Close resources
            pdfDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
