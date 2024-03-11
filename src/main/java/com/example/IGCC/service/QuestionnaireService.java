package com.example.IGCC.service;

import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.QuestionnaireComponent;
import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.repository.QuestionnaireRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
@Slf4j
public class QuestionnaireService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private TemplateEngine templateEngine;
    public ResponseEntity<byte[]> generatePdf() throws Exception {
        List<Questionnaire> noAttributes = questionnaireRepository.findAll();
        List<List<String>> questions = new ArrayList<>();
        for (Questionnaire item : noAttributes) {
            for(QuestionnaireComponent i:item.getComponents()){
                List<String> parts = Arrays.asList(i.getQuestionOpposite().get(0).split("❒"));
                questions.add(parts);
            }
        }
        Context context = new Context();
        context.setVariable("questions", questions);
        String html = templateEngine.process("test-table", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();

        byte[] pdfBytes=outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "test_table.pdf");
        headers.setContentLength(pdfBytes.length);

        log.info("Downloading pdf with test ");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
