package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class GenerateJSONSchema {

    public static void main(String[] args) {
        try {
            String pdfFileName = "4905PIT.pdf";
            // Initialize PdfReader and PdfDocument
            PdfReader pdfReader = new PdfReader(pdfFileName);
            PdfDocument pdfDocument = new PdfDocument(pdfReader);

            // Retrieve AcroForm fields
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDocument, false);
            Map<String, PdfFormField> fields = acroForm.getAllFormFields();

            // Generate and print JSON Schema
            String schemaFileName = pdfFileName.replaceAll("\\.pdf$", ".schema.json");
            String jsonSchema = generateJsonSchema(fields, pdfDocument, schemaFileName);
            System.out.println(jsonSchema);
            writeJsonSchemaToFile(jsonSchema, schemaFileName);

            // Close resources
            pdfDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeJsonSchemaToFile(String jsonSchema, String schemaFileName) {
        // Derive schema file name

        FileWriter fileWriter = null;
        try {
            // Initialize FileWriter
            fileWriter = new FileWriter(new File(schemaFileName));

            // Write JSON Schema to file
            fileWriter.write(jsonSchema);
        } catch (IOException e) {
            // Handle exception
            System.err.println("Failed to write JSON Schema to file: " + e.getMessage());
        } finally {
            try {
                // Ensure FileWriter is closed
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.err.println("Failed to close FileWriter: " + e.getMessage());
            }
        }
    }

    public static String generateJsonSchema(Map<String, PdfFormField> fields, PdfDocument pdfDocument, String schemaFileName) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode schemaNode = mapper.createObjectNode();

        StringBuilder descriptionBuilder = new StringBuilder();
        PdfDocumentInfo documentInfo = pdfDocument.getDocumentInfo();
        descriptionBuilder.append("Subject: ").append(documentInfo.getSubject());
        descriptionBuilder.append(", Keywords: ").append(documentInfo.getKeywords());
        descriptionBuilder.append(", Author: ").append(documentInfo.getAuthor());

        // Set schema properties
        schemaNode.put("$schema", "http://json-schema.org/draft/2019-09/schema#");
        schemaNode.put("$id", "resource:/form/json/schema/"+schemaFileName+".schema.json");
        schemaNode.put("type", "object");
        schemaNode.put("title", documentInfo.getTitle());
        schemaNode.put("description", descriptionBuilder.toString());
        schemaNode.put("additionalProperties", false);

        ObjectNode propertiesNode = mapper.createObjectNode();
        for (Map.Entry<String, PdfFormField> entry : fields.entrySet()) {
            String key = entry.getKey();
            PdfFormField field = entry.getValue();

            ObjectNode fieldNode = mapper.createObjectNode();
            String javaType = determineFieldType(field);
            fieldNode.put("type", javaType);
            if (field.getAlternativeName() != null)
                fieldNode.put("title", field.getAlternativeName().toUnicodeString());
            fieldNode.put("readOnly", field.isReadOnly());
            if (field.getDefaultValue() != null)
                fieldNode.put("defaultValue", field.getDefaultValue().toString());
            fieldNode.put("description", getFieldDescription(field, pdfDocument));
            fieldNode.put("required", field.isRequired());

            // Additional properties can be added based on field attributes
            propertiesNode.set(key, fieldNode);
        }
        schemaNode.set("properties", propertiesNode);

        return schemaNode.toPrettyString();
    }

    private static String getFieldDescription(PdfFormField field, PdfDocument pdfDocument) {
        StringBuilder descriptionBuilder = new StringBuilder();
        int pageNumber = pdfDocument.getPageNumber(field.getWidgets().get(0).getPage());
        descriptionBuilder.append("Page: ").append(pageNumber);
        descriptionBuilder.append(", Mapping Name: ").append(field.getMappingName());
        descriptionBuilder.append(", Multiline: ").append(field.isMultiline());
        descriptionBuilder.append(", Password: ").append(field.isPassword());
        descriptionBuilder.append(", Partial Field Name: ").append(field.getPartialFieldName());
        descriptionBuilder.append(", Field Value: ").append(field.getValueAsString());
        descriptionBuilder.append(", Field Type: ").append(getFieldType(field));
        return descriptionBuilder.toString();
    }

    private static String getFieldType(PdfFormField field) {
        PdfName fieldType = field.getFormType();
        if (PdfName.Btn.equals(fieldType)) {
            if (field instanceof PdfButtonFormField) {
                return "button";
            }
        } else if (PdfName.Ch.equals(fieldType)) {
            return "choice";
        } else if (PdfName.Tx.equals(fieldType)) {
            return "text";
        } else if (PdfName.Sig.equals(fieldType)) {
            return "signature";
        }
        return "unknown";
    }

    private static String determineFieldType(PdfFormField field) {
        String fieldType = getFieldType(field);
        if ("button".equals(fieldType)) {
            return "string";
        } else if ("choice".equals(fieldType)) {
            return "string";
        } else if ("text".equals(fieldType)) {
            return "string";
        } else if ("signature".equals(fieldType)) {
            return "string";
        }
        return "object";
    }

}
