package com.example.IGCC.service;


import com.example.IGCC.entity.Questionnaire;
import com.example.IGCC.entity.QuestionnaireComponent;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionnaireUplodeService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    public void saveDataFromCsv(MultipartFile file) throws IOException {
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(file.getBytes()), Charset.forName("ISO-8859-1"))) {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            MappingIterator<QuestionnaireComponent> iterator = csvMapper.readerFor(QuestionnaireComponent.class).with(schema).readValues(reader);
            List<Questionnaire> questionnaires = processCsvRows(iterator);

            questionnaireRepository.deleteAll();
            questionnaireRepository.saveAll(questionnaires);
            log.info("uploading file {}", file.getOriginalFilename());
        }
    }
    private List<Questionnaire> processCsvRows(MappingIterator<QuestionnaireComponent> iterator) {
        List<Questionnaire> questionnaires = new ArrayList<>();
        List<QuestionnaireComponent> questionnaireComponents = new ArrayList<>();
        String section = null;
        boolean isNewSection = false;

        while (iterator.hasNext()) {
            QuestionnaireComponent csvRow = iterator.next();
            if (!csvRow.getQuesctionSection().isEmpty()) {
                if (isNewSection) {
                    questionnaires.add(createQuestionnaire(section, questionnaireComponents));
                    questionnaireComponents.clear();
                }
                section = csvRow.getQuesctionSection();
                isNewSection = true;
            }
            if (isNewSection) {
                questionnaireComponents.add(createQuestionnaireComponent(csvRow));
            }
        }
        if (section != null) {
            questionnaires.add(createQuestionnaire(section, questionnaireComponents));
        }
        return questionnaires;
    }

    private Questionnaire createQuestionnaire(String section, List<QuestionnaireComponent> components) {
        return new Questionnaire(section, new ArrayList<>(components));
    }

    private QuestionnaireComponent createQuestionnaireComponent(QuestionnaireComponent csvRow) {
        QuestionnaireComponent component = new QuestionnaireComponent();
        component.setQuestionId(UUID.randomUUID().toString());
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
