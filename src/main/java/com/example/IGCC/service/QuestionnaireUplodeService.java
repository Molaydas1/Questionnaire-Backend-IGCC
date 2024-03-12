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

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionnaireUplodeService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
//    public void saveDataFromCsv(MultipartFile file) throws IOException, CsvException {
//        if ((!file.getOriginalFilename().endsWith(".csv"))) {
//            log.info("Please select a file to upload.");
//            throw new MyFileNotFoundException("Please select a .csv file to upload.");
//        }
//        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
//        CSVReader csvReader = new CSVReader(br);
//
//        List<String[]> lines = csvReader.readAll(); // Read all lines from CSV
//
//        List<Questionnaire> questionnaires = new ArrayList<>();
//        List<QuestionnaireComponent> components = new ArrayList<>();
//        Questionnaire questionnaire = null;
//        QuestionnaireComponent component;
//        int count = 0;
//        int sectionId = 1;
//        for (String[] fields : lines.subList(1, lines.size())) {
//            String row_1 = fields[0];
//            String row_2 = fields[1];
//            if (row_1.equals("") && row_2.equals("")) {
//                count++;
//                if (count == 2) {
//                    continue;
//                }
//                questionnaire.setComponents(components);
//                questionnaires.add(questionnaire);
//                components = new ArrayList<>();
//            } else if ((!row_1.equals("")) && (!row_2.equals(""))) {
//                count = 0;
//                questionnaire = new Questionnaire();
//                questionnaire.setId(String.valueOf(sectionId++));
//                questionnaire.setSection(fields[2]);
//            } else if (row_1.equals("") && (!row_2.equals(""))) {
//                component = new QuestionnaireComponent();
//                component.setQuestionId(String.valueOf(UUID.randomUUID().toString()));
//                component.setQuestion(fields[2]);
//                component.setScore(fields[3]);
//                component.setQuestionType(fields[4]);
//                component.setRequired(Boolean.valueOf(fields[5]));
//                component.setReport(fields[6]);
//                List<String> list=new ArrayList<>();
//                for(int i=7;i<fields.length;i++){
//                    if(fields[i].equals(""))break;
//                    list.add(fields[i]);
//                }
//                component.setQuestionOpposite(list);
//                components.add(component);
//            }
//        }
//        questionnaireRepository.saveAll(questionnaires);
//    }

    public void importBulk(byte[] csvFile) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        List<Questionnaire> questionnaires = new ArrayList<>();
        List<QuestionnaireComponent> questionnaireComponents =new ArrayList<>();
        Charset charset = Charset.forName("ISO-8859-1");
        try (Reader reader = new InputStreamReader(new ByteArrayInputStream(csvFile), charset)) {
            MappingIterator<QuestionnaireCSVModel> iterator = csvMapper.readerFor(QuestionnaireCSVModel.class).with(schema).readValues(reader);
            boolean count=false;
            String QuesctionSection="";
            while (iterator.hasNext()) {
                QuestionnaireCSVModel csvRow = iterator.next();
                QuestionnaireComponent questionnaireComponent=new QuestionnaireComponent();
                if(!csvRow.getQuesctionSection().equals("")){
                    if(count){
                        questionnaires.add(new Questionnaire(UUID.randomUUID().toString(),
                                QuesctionSection,questionnaireComponents));
                        questionnaireComponents=new ArrayList<>();
                    }
                    QuesctionSection=csvRow.getQuesctionSection();
                    count=true;
                }if (count) {
                    questionnaireComponent.setQuestionId(csvRow.getQuestionId());
                    questionnaireComponent.setQuestion(csvRow.getQuestion());
                    questionnaireComponent.setReport(csvRow.getReport());
                    questionnaireComponent.setQuestionType(csvRow.getQuestionType());
                    questionnaireComponent.setRequired(Boolean.valueOf(csvRow.getReport()));
                    questionnaireComponent.setScore(csvRow.getScore());
                    questionnaireComponent.setIfNo(csvRow.getIfNo());
                    questionnaireComponent.setIfYes(csvRow.getIfYes());
                    questionnaireComponents.add(questionnaireComponent);
                }
                if(!iterator.hasNext()){
                    questionnaires.add(new Questionnaire(UUID.randomUUID().toString(),
                            QuesctionSection,questionnaireComponents));
                }
            }
        }
        questionnaireRepository.deleteAll();
         questionnaireRepository.saveAll(questionnaires);
    }

}
