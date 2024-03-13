package com.example.IGCC.controller;

import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.service.QuestionnaireService;
import com.example.IGCC.service.QuestionnaireUplodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/Questionnaire")
@Slf4j
public class QuestionnaireController {
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionnaireUplodeService questionnaireUplodeService;
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        try {
            questionnaireUplodeService.saveDataFromCsv(file.getBytes());
            log.info("uploading file {}", file.getOriginalFilename());
            return ResponseEntity.ok("Excel data imported successfully.");
        }catch (Exception e) {
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import Excel data.");
        }
    }
    @GetMapping("/generatePdf")
    public ResponseEntity<byte[]> generatePdf(){
        try {
            log.info("Downloading pdf with test ");
            return questionnaireService.generatePdf();
        }catch(Exception e){
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/get")
    public ResponseEntity<List<Questionnaire>> getQuestionnaire(@RequestParam("email")String email){
        try {
            List<Questionnaire> questionnaires=questionnaireService.getQuestionnaire(email);
            log.info("Downloading pdf with test ");
            return ResponseEntity.status(HttpStatus.OK).body(questionnaires);
        }catch(Exception e){
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}


