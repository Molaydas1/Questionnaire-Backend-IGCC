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
    public void uploadData(MultipartFile file) throws IOException {
        List<List<String>> rows = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        rows = StreamSupport.stream(sheet.spliterator(), false)
                .map(row -> StreamSupport
                        .stream(row.spliterator(), false)
                        .map(this::getCellStringValue)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        List<Questionnaire> questionnaires=new ArrayList<>();
        List<QuestionnaireComponent> components=new ArrayList<>();
        Questionnaire questionnaire=null;
        QuestionnaireComponent component;
        int count=0;
        int sectionId=1;
        int questionId=1;
        for(List row:rows){
            String row_1= (String) row.get(0);
            String row_2= (String) row.get(1);
            if(row_1==null && row_2==null){
                count++;
                if(count==2){
                    break;
                }
                questionnaire.setComponents(components);
                questionnaires.add(questionnaire);
                components=new ArrayList<>();
            } else if(row_1!=null && row_2!=null) {
                count=0;
                questionnaire=new Questionnaire();
                questionnaire.setId(String.valueOf(sectionId++));
                questionnaire.setSection((String) row.get(2));
                questionId=1;
            } else if(row_1==null && row_2!=null) {
                component=new QuestionnaireComponent();
                component.setQuestionId(String.valueOf(questionId++));
                component.setQuestion((String) row.get(2));
                component.setScore((String) row.get(3));
                component.setReport((String) row.get(5));
                component.setQuestionType("Radio");
                component.setRequired(true);
                Map<String, String> map = new HashMap<>();
                map.put("no", (String) row.get(6));
                map.put("yes", (String) row.get(7));
                component.setParameters(map);
                components.add(component);
            }
        }
        questionnaireRepository.saveAll(questionnaires);
    }
    private String getCellStringValue(Cell cell) {
        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            return cell.getStringCellValue().replace("'","\'");
        } else if (cellType == CellType.NUMERIC) {
            return String.valueOf((int)cell.getNumericCellValue());
        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        }
        return null;
    }
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
