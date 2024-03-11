package com.example.IGCC.service;

import com.example.IGCC.exception.MyFileNotFoundException;
import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.CountryOrSectorRisks;
import com.example.IGCC.model.Questionnaire;
import com.example.IGCC.model.QuestionnaireComponent;
import com.example.IGCC.repository.CountryOrSectorRisksRepository;
import com.lowagie.text.DocumentException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CountryOrSectorRisksService {
    @Autowired
    private CountryOrSectorRisksRepository countryOrSectorRisksRepository;
    @Autowired
    private TemplateEngine templateEngine;
    public void saveDataFromCsv(MultipartFile file) throws IOException, CsvException {
        if ((!file.getOriginalFilename().endsWith(".csv"))) {
            log.info("Please select a file to upload.");
            throw new MyFileNotFoundException("Please select a .xlsx file to upload.");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVReader csvReader = new CSVReader(br);

        List<String[]> lines = csvReader.readAll();
        List<CountryOrSectorRisks> countryOrSectorRisks = new ArrayList<>();

        for (String[] fields : lines.subList(2, lines.size())) {
            countryOrSectorRisks.add(new CountryOrSectorRisks(
                    fields[0],fields[1],fields[2],fields[3],fields[4],fields[5]
            ));
        }
        countryOrSectorRisksRepository.saveAll(countryOrSectorRisks);
    }
    public byte[] generatePdf(String country) throws NoRecordsFoundExcption, DocumentException {

        List<CountryOrSectorRisks> countryOrSectorRisks = countryOrSectorRisksRepository.findAllByCountry(country);
        if(countryOrSectorRisks.isEmpty())throw new NoRecordsFoundExcption("no records found");

        Context context = new Context();
        context.setVariable("output", countryOrSectorRisks);
        String html = templateEngine.process("country-or-sector-risk-table", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();

        return outputStream.toByteArray();
    }
}
