package com.example.IGCC.service;

import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.QuestionnaireComponent;
import com.example.IGCC.repository.QuestionnaireRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionnaireUplodeService {
    private List<List<String>> convertExcelToCsv(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<List<String>> csvData = new ArrayList<>();

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowData.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        case BOOLEAN:
                            rowData.add(String.valueOf(cell.getBooleanCellValue()));
                            break;
                        case BLANK:
                            rowData.add("");
                            break;
                        default:
                            rowData.add("");
                            break;
                    }
                }
                csvData.add(rowData);
            }
            return csvData;
        }
    }
    public List<Questionnaire> parseCsvData(MultipartFile file) throws IOException {
        List<List<String>> csvData=convertExcelToCsv(file);
        List<Questionnaire> questionnaires=new ArrayList<>();
        List<QuestionnaireComponent> components=new ArrayList<>();
        Questionnaire questionnaire=null;
        QuestionnaireComponent component;
        int count=0;
        int sectionId=1;
        int questionId=1;
        for (List<String> row : csvData) {
            String row_1= row.get(0);
            String row_2= row.get(1);
            if(row_1=="" && row_2==""){
                count++;
                if(count==2){
                    break;
                }
                questionnaire.setComponents(components);
                questionnaires.add(questionnaire);
                components=new ArrayList<>();
            } else if(row_1!="" && row_2!="") {
                count=0;
                questionnaire=new Questionnaire();
                questionnaire.setId(String.valueOf(sectionId++));
                questionnaire.setSection(row.get(2));
                questionId=1;
            } else if(row_1=="" && row_2!="") {
                component=new QuestionnaireComponent();
                component.setQuestionId(String.valueOf(questionId++));
                component.setQuestion(row.get(2));
                component.setScore(row.get(3));
                component.setReport(row.get(5));
                component.setQuestionType("Radio");
                component.setRequired(true);
                Map<String, String> map = new HashMap<>();
                map.put("no",row.get(6));
                map.put("yes",row.get(7));
                component.setParameters(map);
                components.add(component);
            }
        }
        return questionnaires;
    }

}
