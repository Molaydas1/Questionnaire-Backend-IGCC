package com.example.IGCC.service;

import com.example.IGCC.entity.Questionnaire;
import com.example.IGCC.entity.QuestionnaireComponent;
import com.example.IGCC.entity.User;
import com.example.IGCC.entity.UserAnswer;
import com.example.IGCC.model.*;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.example.IGCC.repository.UserRepository;
import com.lowagie.text.DocumentException;
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
    @Autowired
    private UserRepository userRepository;
    public ResponseEntity<byte[]> generatePdf() throws DocumentException {
        List<Questionnaire> noAttributes = questionnaireRepository.findAll();
        List<List<String>> questions = new ArrayList<>();
        for (Questionnaire item : noAttributes) {
            for(QuestionnaireComponent i:item.getComponents()){
                List<String> parts = Arrays.asList(i.getIfNo().split("â\u009D\u0092"));
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
    public ResponseEntity<?> getQuestionnaire(String email){
        User user=userRepository.findByEmail(email);
        List<QuestionnaireResponse> questionnaires = questionnaireRepository.findAllQuestionnaire();
        if (questionnaires.isEmpty())
            return new ResponseEntity<>(new ApiResponse<>(false,"List of Questionnaire not found ",null), HttpStatus.OK);
        if(!user.getUserAnswerResponse().isEmpty()){
            log.info("already user exists{}", user);
            boolean count;
            for(QuestionnaireResponse questionnaire:questionnaires){
                for(QuestionnaireComponentResponse component:questionnaire.getComponents()){
                    count=false;
                    for (UserAnswer userAnswerResponse:user.getUserAnswerResponse()){
                        if(userAnswerResponse.getQuestionId().equals(component.getQuestionId())){
                            component.setResponse(userAnswerResponse.getResponse());
                            count=true;
                            break;
                        }
                    }if(count==false){
                        component.setResponse(null);
                    }
                }
            }
            return new ResponseEntity<>(new ApiResponse<>(true,"List of Questionnaire found ",questionnaires), HttpStatus.OK);

        }
        log.info("get all Questionnaire");
        return new ResponseEntity<>(new ApiResponse<>(true,"List of Questionnaire found ",questionnaires), HttpStatus.OK);

    }
}
