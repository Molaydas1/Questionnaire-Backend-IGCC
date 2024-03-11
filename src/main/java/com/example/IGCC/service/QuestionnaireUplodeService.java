package com.example.IGCC.service;

import com.example.IGCC.exception.MyFileNotFoundException;
import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.QuestionnaireComponent;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class QuestionnaireUplodeService {
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    public void saveDataFromCsv(MultipartFile file) throws IOException, CsvException {
        if ((!file.getOriginalFilename().endsWith(".csv"))) {
            log.info("Please select a file to upload.");
            throw new MyFileNotFoundException("Please select a .csv file to upload.");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVReader csvReader = new CSVReader(br);

        List<String[]> lines = csvReader.readAll(); // Read all lines from CSV

        List<Questionnaire> questionnaires = new ArrayList<>();
        List<QuestionnaireComponent> components = new ArrayList<>();
        Questionnaire questionnaire = null;
        QuestionnaireComponent component;
        int count = 0;
        int sectionId = 1;
        for (String[] fields : lines.subList(1, lines.size())) {
            String row_1 = fields[0];
            String row_2 = fields[1];
            if (row_1.equals("") && row_2.equals("")) {
                count++;
                if (count == 2) {
                    continue;
                }
                questionnaire.setComponents(components);
                questionnaires.add(questionnaire);
                components = new ArrayList<>();
            } else if ((!row_1.equals("")) && (!row_2.equals(""))) {
                count = 0;
                questionnaire = new Questionnaire();
                questionnaire.setId(String.valueOf(sectionId++));
                questionnaire.setSection(fields[2]);
            } else if (row_1.equals("") && (!row_2.equals(""))) {
                component = new QuestionnaireComponent();
                component.setQuestionId(String.valueOf(UUID.randomUUID().toString()));
                component.setQuestion(fields[2]);
                component.setScore(fields[3]);
                component.setReport(fields[5]);
                component.setQuestionType("Radio");
                component.setRequired(true);
                List<String> list=new ArrayList<>();
                list.add(fields[6]);
                list.add(fields[7]);
                component.setQuestionOpposite(list);
                components.add(component);
            }
        }
        questionnaireRepository.saveAll(questionnaires);
    }
}
