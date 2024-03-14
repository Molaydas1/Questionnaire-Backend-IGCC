package com.example.IGCC.controller;

import com.example.IGCC.model.ApiResponse;
import com.example.IGCC.service.QuestionnaireService;
import com.example.IGCC.service.QuestionnaireUplodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/Questionnaire")
public class QuestionnaireController {
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionnaireUplodeService questionnaireUplodeService;
    @RequestMapping(path = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCsvFile(@RequestParam("file") MultipartFile file) throws IOException {
        questionnaireUplodeService.saveDataFromCsv(file);
        return new ResponseEntity<>(new ApiResponse<>(true,"uploading file successfully",null), HttpStatus.OK);
    }
    @GetMapping("/generatePdf")
    public ResponseEntity<byte[]> generatePdf() throws Exception {
        return questionnaireService.generatePdf();
    }
    @GetMapping("/get")
    public ResponseEntity<?> getQuestionnaire(@RequestParam("email")String email) {
        return questionnaireService.getQuestionnaire(email);
    }

}


