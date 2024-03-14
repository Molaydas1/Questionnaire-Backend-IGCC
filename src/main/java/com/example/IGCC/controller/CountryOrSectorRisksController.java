package com.example.IGCC.controller;

import com.example.IGCC.service.CountryOrSectorRisksService;
import com.lowagie.text.DocumentException;
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
@RequestMapping("/CountryOrSectorRisks")
public class CountryOrSectorRisksController {
    @Autowired
    private CountryOrSectorRisksService countryOrSectorRisksService;
    @RequestMapping(path = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCsvFile(@RequestPart("file") MultipartFile file) throws IOException {
            return countryOrSectorRisksService.saveDataFromCsv(file);
    }
    @RequestMapping(value = "/generatePdf", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generatePdfByCountry(@RequestParam("country") String country) throws DocumentException {
        return countryOrSectorRisksService.generatePdfByCountry(country);
    }
}