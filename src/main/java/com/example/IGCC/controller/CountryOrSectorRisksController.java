package com.example.IGCC.controller;

import com.example.IGCC.service.CountryOrSectorRisksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/CountryOrSectorRisks")
@Slf4j
public class CountryOrSectorRisksController {
    @Autowired
    private CountryOrSectorRisksService countryOrSectorRisksService;
//    @PostMapping("/upload")
    @RequestMapping(path = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCsvFile(@RequestPart("file") MultipartFile file){
        try {
            countryOrSectorRisksService.saveDataFromCsv(file.getInputStream());
            log.info("uploading file {}", file.getOriginalFilename());
            return ResponseEntity.ok("Excel data imported successfully.");
        } catch (Exception e) {
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import Excel data.");
        }
    }
    @GetMapping("/generatePdf")
    public ResponseEntity<byte[]> GeneratePdfByCountry(
            @RequestParam("country") String country){
        try {
            return countryOrSectorRisksService.generatePdfByCountry(country);
        }catch(Exception e){
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}