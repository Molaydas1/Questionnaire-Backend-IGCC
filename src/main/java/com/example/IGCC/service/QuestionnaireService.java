package com.example.IGCC.service;

import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.QuestionnaireComponent;
import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.User;
import com.example.IGCC.model.UserAnswerResponse;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.example.IGCC.repository.UserRepository;
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
    public ResponseEntity<byte[]> generatePdf() throws Exception {
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
//    public List<Questionnaire> getQuestionnaireService(String email){
//        User user=userRepository.findByEmail(email);
//        List<Questionnaire> questionnaires = questionnaireRepository.findAllQuestionnaire();
//        if(user.getUserAnswerResponse()!=null){
//            log.info("already user exists{}", user);
//            boolean count;
//            for(Questionnaire questionnaire:questionnaires){
//                for(QuestionnaireComponent component:questionnaire.getComponents()){
//                    count=false;
//                    for (UserAnswerResponse userAnswerResponse:user.getUserAnswerResponse()){
//                        if(userAnswerResponse.getQuestionId().equals(component.getQuestionId())){
//                            component.setResponse(userAnswerResponse.getResponse());
//                            count=true;
//                        }else if (count==true){
//                            break;
//                        }
//                    }if(count==false){
//                        List<Boolean> list=new ArrayList<>();
//                        for(Boolean s:component.getResponse()){
//                            list.add(false);
//                        }
//                        component.setResponse(list);
//                    }
//                }
//            }
//            return questionnaires;
//        }
//        for(Questionnaire questionnaire:questionnaires){
//            for(QuestionnaireComponent component:questionnaire.getComponents()){
//                List<Boolean> list=new ArrayList<>();
//                for(boolean s:component.getResponse()){
//                    list.add(false);
//                }
//                component.setResponse(list);
//            }
//        }
//        log.info("create user {}", user);
//        user.setUserAnswerResponse(new ArrayList<>());
//        userRepository.save(user);
//        return questionnaires;
//    }
//    public User userAndQuestionnaireSave(List<Questionnaire> questionnaires,String email){
//        User user=userRepository.findByEmail(email);
//        if(user==null)throw new NoRecordsFoundExcption("Please select a .csv file to upload.");
//        List<UserAnswerResponse> userAnswerResponses=new ArrayList<>();
//        if(user.getUserAnswerResponse().isEmpty()){
//            for(Questionnaire questionnaire:questionnaires){
//                for(QuestionnaireComponent component:questionnaire.getComponents()){
//                    List<Boolean> list=new ArrayList<>();
//                    for(String s:component.getQuestionOpposite()){
//                        list.add(false);
//                    }
//                    userAnswerResponses.add(new UserAnswerResponse(component.getQuestionId(),list));
//                }
//            }
//        }else{
//            boolean count;
//            int i=0;
//            userAnswerResponses=user.getUserAnswerResponse();
//            for(Questionnaire questionnaire:questionnaires){
//                for(QuestionnaireComponent component:questionnaire.getComponents()){
//                    count=false;
//                    for(UserAnswerResponse userAnswerResponse:userAnswerResponses){
//                        if(userAnswerResponse.getQuestionId().equals(component.getQuestionId())){
//                            count=true;
//                            userAnswerResponses.set(i,new UserAnswerResponse(userAnswerResponse.getQuestionId(),component.getResponse()));
//                        }else if (count==true){
//                            break;
//                        }
//                    }if(count==false){
//                        userAnswerResponses.add(new UserAnswerResponse(component.getQuestionId(),component.getResponse()));
//                    }
//                    i++;
//                }
//            }
//        }
//        user.setUserAnswerResponse(userAnswerResponses);
//        return user;
//    }
}
