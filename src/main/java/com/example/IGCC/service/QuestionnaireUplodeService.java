package com.example.IGCC.service;


import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.QuestionnaireCSVModel;
import com.example.IGCC.model.QuestionnaireComponent;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionnaireUplodeService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
//    public void saveDataFromCsv(byte[] csvFile) throws IOException {
//        CsvMapper csvMapper = new CsvMapper();
//        CsvSchema schema = CsvSchema.emptySchema().withHeader();
//        List<Questionnaire> questionnaires = new ArrayList<>();
//        List<QuestionnaireComponent> questionnaireComponents =new ArrayList<>();
//        Charset charset = Charset.forName("ISO-8859-1");
//        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(csvFile), charset)) {
//            MappingIterator<QuestionnaireCSVModel> iterator = csvMapper.readerFor(QuestionnaireCSVModel.class).with(schema).readValues(reader);
//            boolean count=false;
//            String QuesctionSection="";
//            while (iterator.hasNext()) {
//                QuestionnaireCSVModel csvRow = iterator.next();
//                QuestionnaireComponent questionnaireComponent=new QuestionnaireComponent();
//                if(!csvRow.getQuesctionSection().equals("")){
//                    if(count){
//                        questionnaires.add(new Questionnaire(UUID.randomUUID().toString(),
//                                QuesctionSection,questionnaireComponents));
//                        questionnaireComponents=new ArrayList<>();
//                    }
//                    QuesctionSection=csvRow.getQuesctionSection();
//                    count=true;
//                }if (count) {
//                    questionnaireComponent.setQuestionId(csvRow.getQuestionId());
//                    questionnaireComponent.setQuestion(csvRow.getQuestion());
//                    questionnaireComponent.setReport(csvRow.getReport());
//                    questionnaireComponent.setQuestionType(csvRow.getQuestionType());
//                    questionnaireComponent.setRequired(Boolean.valueOf(csvRow.getReport()));
//                    questionnaireComponent.setScore(csvRow.getScore());
//                    questionnaireComponent.setIfNo(csvRow.getIfNo());
//                    questionnaireComponent.setIfYes(csvRow.getIfYes());
//                    questionnaireComponents.add(questionnaireComponent);
//                }
//                if(!iterator.hasNext()){
//                    questionnaires.add(new Questionnaire(UUID.randomUUID().toString(),
//                            QuesctionSection,questionnaireComponents));
//                }
//            }
//        }
//        questionnaireRepository.deleteAll();
//         questionnaireRepository.saveAll(questionnaires);
//    }

    ///////////////////////////////////////////

    public void saveDataFromCsv(byte[] csvFile) throws IOException {
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(csvFile), Charset.forName("ISO-8859-1"))) {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<QuestionnaireCSVModel> iterator = csvMapper.readerFor(QuestionnaireCSVModel.class).with(schema).readValues(reader);

            List<Questionnaire> questionnaires = processCsvRows(iterator);

            questionnaireRepository.deleteAll();
            questionnaireRepository.saveAll(questionnaires);
        }
    }
    private List<Questionnaire> processCsvRows(MappingIterator<QuestionnaireCSVModel> iterator) {
        List<Questionnaire> questionnaires = new ArrayList<>();
        List<QuestionnaireComponent> questionnaireComponents = new ArrayList<>();
        String section = null;
        String id=null;
        boolean isNewSection = false;

        while (iterator.hasNext()) {
            QuestionnaireCSVModel csvRow = iterator.next();
            if (!csvRow.getQuesctionSection().isEmpty()) {
                if (isNewSection) {
                    questionnaires.add(createQuestionnaire(id,section, questionnaireComponents));
                    questionnaireComponents.clear();
                }
                section = csvRow.getQuesctionSection();
                id = csvRow.getQuestionId().split("\\.")[0];
                isNewSection = true;
            }
            if (isNewSection) {
                questionnaireComponents.add(createQuestionnaireComponent(csvRow));
            }
        }
        if (section != null) {
            questionnaires.add(createQuestionnaire(id,section, questionnaireComponents));
        }
        return questionnaires;
    }

    private Questionnaire createQuestionnaire(String id,String section, List<QuestionnaireComponent> components) {
        return new Questionnaire(id, section, new ArrayList<>(components));
    }

    private QuestionnaireComponent createQuestionnaireComponent(QuestionnaireCSVModel csvRow) {
        QuestionnaireComponent component = new QuestionnaireComponent();
        component.setQuestionId(csvRow.getQuestionId().split("\\.")[1]);
        component.setQuestion(csvRow.getQuestion());
        component.setReport(csvRow.getReport());
        component.setQuestionType(csvRow.getQuestionType());
        component.setRequired(Boolean.valueOf(csvRow.getReport()));
        component.setScore(csvRow.getScore());
        component.setIfNo(csvRow.getIfNo());
        component.setIfYes(csvRow.getIfYes());
        return component;
    }

}
