package com.example.IGCC.service;


import com.example.IGCC.entity.Question;
import com.example.IGCC.entity.QuestionSection;
import com.example.IGCC.entity.Questionnaire;
import com.example.IGCC.entity.QuestionnaireComponent;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
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

//            questionnaireRepository.updateAll();

            questionnaireRepository.save(processCsvRows(iterator));
            log.info("uploading file {}", file.getOriginalFilename());
        }
    }
    private Questionnaire processCsvRows(MappingIterator<QuestionnaireComponent> iterator) {
        List<QuestionSection> questionSections = new ArrayList<>();
        String section = null;
        boolean isNewSection = false;
        List<Question> questions=new ArrayList<>();
        while (iterator.hasNext()) {
            QuestionnaireComponent csvRow = iterator.next();
            if (!csvRow.getQuesctionSection().isEmpty()) {
                if(isNewSection){
                    questionSections.add(new QuestionSection(UUID.randomUUID().toString(),
                            section,questions));
                    questions=new ArrayList<>();
                    questions.add(new Question(UUID.randomUUID().toString(),csvRow.getQuestion(),
                            csvRow.getScore(),csvRow.getQuestionType(),csvRow.getRequired(),
                            csvRow.getReport(),csvRow.getIfNo(),csvRow.getIfYes()));
                }
                section=csvRow.getQuesctionSection();
                if(isNewSection)
                    continue;
                isNewSection = true;
            }if(isNewSection){
                questions.add(new Question(UUID.randomUUID().toString(),csvRow.getQuestion(),
                        csvRow.getScore(),csvRow.getQuestionType(),csvRow.getRequired(),
                        csvRow.getReport(),csvRow.getIfNo(),csvRow.getIfYes()));
            }
        }questionSections.add(new QuestionSection(UUID.randomUUID().toString(),
                section,questions));

        return new Questionnaire(UUID.randomUUID().toString(),new Date(),true,questionSections);
    }

}
