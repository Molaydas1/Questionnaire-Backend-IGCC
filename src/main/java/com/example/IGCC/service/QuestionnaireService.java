package com.example.IGCC.service;

import com.example.IGCC.model.QuestionnaireComponent;
import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.repository.QuestionnaireRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class QuestionnaireService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private TemplateEngine templateEngine;
    public byte[] generatePdf() throws Exception {
        List<Questionnaire> noAttributes = questionnaireRepository.findAll();
        List<List<String>> questions = new ArrayList<>();
        for (Questionnaire item : noAttributes) {
            for(QuestionnaireComponent i:item.getComponents()){
                List<String> parts = Arrays.asList(i.getParameters().get("no").split("❒"));
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

        return outputStream.toByteArray();
    }
}
