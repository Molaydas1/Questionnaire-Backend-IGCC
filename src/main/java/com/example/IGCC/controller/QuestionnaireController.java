package com.example.IGCC.controller;

import com.example.IGCC.exception.MyFileNotFoundException;
import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.QuestionnaireComponent;
import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.repository.QuestionnaireRepository;
import com.example.IGCC.service.QuestionnaireService;
import com.example.IGCC.service.QuestionnaireUplodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/Questionnaire")
@Slf4j
public class QuestionnaireController {
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionnaireRepository questionnaireRepository;
    @Autowired
    private QuestionnaireUplodeService questionnaireUplodeService;
    //    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        if ((!file.getOriginalFilename().endsWith(".xlsx"))) {
//            log.info("Please select a file to upload.");
//            throw new MyFileNotFoundException("Please select a .xlsx file to upload.");
//        }
//        try {
//            questionnaireUplodeService.parseCsvData(file);
//            log.info("uploading file {}", file.getOriginalFilename());
//            return ResponseEntity.ok("Excel data imported successfully.");
//        } catch (IOException e) {
//            log.info("Execption occurs {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import Excel data.");
//        }
//    }
    @PostMapping("/upload")
    public ResponseEntity<String> convertExcelToCsvAndSave(@RequestParam("file") MultipartFile file) {
//        if ((!file.getOriginalFilename().endsWith(".xlsx"))) {
//            log.info("Please select a file to upload.");
//            throw new MyFileNotFoundException("Please select a .xlsx file to upload.");
//        }
        try {
//            questionnaireRepository.saveAll(questionnaireUplodeService.parseCsvData(file));
            questionnaireRepository.saveAll(questionnaireUplodeService.saveDataFromCsv(file));
            log.info("uploading file {}", file.getOriginalFilename());
            return ResponseEntity.ok("Excel data imported successfully.");
        } catch (IOException e) {
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import Excel data.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/getAllQuestionnaire")
    public ResponseEntity<List<Questionnaire>> getAllQuestionnaire() {
        List<Questionnaire> questionnaires = questionnaireRepository.findAllQuestionnaire();
        if (questionnaires.isEmpty()) {
            log.info("no records found");
            throw new NoRecordsFoundExcption("no records found");
        }
        log.info("get all question");
        return ResponseEntity.ok().body(questionnaires);
    }
    @GetMapping("/generatePdf")
    public ResponseEntity<byte[]> generatePdf(){
        try {
            byte[] pdfBytes = questionnaireService.generatePdf();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "test_table.pdf");
            headers.setContentLength(pdfBytes.length);

            log.info("Downloading pdf with test ");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        }catch(Exception e){
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


