package com.example.IGCC.controller;

import com.example.IGCC.exception.MyFileNotFoundException;
import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.CountryOrSectorRisks;
import com.example.IGCC.repository.CountryOrSectorRisksRepository;
import com.example.IGCC.service.CountryOrSectorRisksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/CountryOrSectorRisks")
@Slf4j
public class CountryOrSectorRisksController {
    @Autowired
    private CountryOrSectorRisksService countryOrSectorRisksService;
    @Autowired
    private CountryOrSectorRisksRepository countryOrSectorRisksRepository;

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file){
//        if ((!file.getOriginalFilename().endsWith(".xlsx"))) {
//            log.info("Please select a file to upload.");
//            throw new MyFileNotFoundException("Please select a .xlsx file to upload.");
//        }
//        try {
//            countryOrSectorRisksService.importDataFromExcel(file);
//            log.info("uploading file {}", file.getOriginalFilename());
//            return ResponseEntity.ok("Excel data imported successfully.");
//        } catch (IOException e) {
//            log.info("Execption occurs {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import Excel data.");
//        }
//    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file){
        try {
            countryOrSectorRisksService.saveDataFromCsv(file);
            log.info("uploading file {}", file.getOriginalFilename());
            return ResponseEntity.ok("Excel data imported successfully.");
        } catch (Exception e) {
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import Excel data.");
        }
    }
    @GetMapping("/generatePdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam("country") String country){
        try {
            byte[] pdfBytes = countryOrSectorRisksService.generatePdf(country);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "country_or_sector_risk_table.pdf");
            headers.setContentLength(pdfBytes.length);

            log.info("Downloading pdf with Country {}", country);
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        }catch(Exception e){
            log.info("Execption occurs {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}