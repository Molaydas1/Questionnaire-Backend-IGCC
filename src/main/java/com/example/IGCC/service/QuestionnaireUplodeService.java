package com.example.IGCC.service;

import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.QuestionnaireComponent;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class QuestionnaireUplodeService {

    //    public void uploadData(MultipartFile file) throws IOException {
//        List<List<String>> rows = new ArrayList<>();
//
//        Workbook workbook = WorkbookFactory.create(file.getInputStream());
//        Sheet sheet = workbook.getSheetAt(0);
//        rows = StreamSupport.stream(sheet.spliterator(), false)
//                .map(row -> StreamSupport
//                        .stream(row.spliterator(), false)
//                        .map(this::getCellStringValue)
//                        .collect(Collectors.toList()))
//                .collect(Collectors.toList());
//
//        List<Questionnaire> questionnaires=new ArrayList<>();
//        List<QuestionnaireComponent> components=new ArrayList<>();
//        Questionnaire questionnaire=null;
//        QuestionnaireComponent component;
//        int count=0;
//        int sectionId=1;
//        int questionId=1;
//        for(List row:rows){
//            String row_1= (String) row.get(0);
//            String row_2= (String) row.get(1);
//            if(row_1==null && row_2==null){
//                count++;
//                if(count==2){
//                    break;
//                }
//                questionnaire.setComponents(components);
//                questionnaires.add(questionnaire);
//                components=new ArrayList<>();
//            } else if(row_1!=null && row_2!=null) {
//                count=0;
//                questionnaire=new Questionnaire();
//                questionnaire.setId(String.valueOf(sectionId++));
//                questionnaire.setSection((String) row.get(2));
//                questionId=1;
//            } else if(row_1==null && row_2!=null) {
//                component=new QuestionnaireComponent();
//                component.setQuestionId(String.valueOf(questionId++));
//                component.setQuestion((String) row.get(2));
//                component.setScore((String) row.get(3));
//                component.setReport((String) row.get(5));
//                component.setQuestionType("Radio");
//                component.setRequired(true);
//                Map<String, String> map = new HashMap<>();
//                map.put("no", (String) row.get(6));
//                map.put("yes", (String) row.get(7));
//                component.setParameters(map);
//                components.add(component);
//            }
//        }
//        questionnaireRepository.saveAll(questionnaires);
//    }
//    private String getCellStringValue(Cell cell) {
//        CellType cellType = cell.getCellType();
//        if (cellType == CellType.STRING) {
//            return cell.getStringCellValue().replace("'","\'");
//        } else if (cellType == CellType.NUMERIC) {
//            return String.valueOf((int)cell.getNumericCellValue());
//        } else if (cellType == CellType.BOOLEAN) {
//            return String.valueOf(cell.getBooleanCellValue());
//        }
//        return null;
//    }
    private List<List<String>> convertExcelToCsv(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            return StreamSupport.stream(sheet.spliterator(), false)
                    .map(row -> StreamSupport.stream(row.spliterator(), false)
                            .map(cell -> {
                                switch (cell.getCellType()) {
                                    case STRING:
                                        return cell.getStringCellValue();
                                    case NUMERIC:
                                        return String.valueOf(cell.getNumericCellValue());
                                    case BOOLEAN:
                                        return String.valueOf(cell.getBooleanCellValue());
                                    default:
                                        return "";
                                }
                            })
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());
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
            } else if(row_1=="" && row_2!="") {
                component=new QuestionnaireComponent();
                component.setQuestionId(String.valueOf(UUID.randomUUID().toString()));
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
