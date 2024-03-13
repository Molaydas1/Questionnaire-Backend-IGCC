package com.example.IGCC.service;

import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.CountryOrSectorRisks;
import com.example.IGCC.repository.CountryOrSectorRisksRepository;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CountryOrSectorRisksService {
    @Autowired
    private CountryOrSectorRisksRepository countryOrSectorRisksRepository;
    @Autowired
    private TemplateEngine templateEngine;
    public void saveDataFromCsv(InputStream csvFile) throws IOException{
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        List<CountryOrSectorRisks> validRows = new ArrayList<>();

        try (MappingIterator<CountryOrSectorRisks> iterator = csvMapper.readerFor(CountryOrSectorRisks.class).with(schema).readValues(csvFile)) {
            while (iterator.hasNext()) {
                CountryOrSectorRisks csvRow = iterator.next();
                if (!csvRow.getId().isEmpty())
                    validRows.add(csvRow);
            }
            countryOrSectorRisksRepository.saveAll(validRows);
        }
    }
    public ResponseEntity<byte[]> generatePdfByCountry(String country) throws NoRecordsFoundExcption, DocumentException {

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
        byte[] pdfBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "country_or_sector_risk_table.pdf");
        headers.setContentLength(pdfBytes.length);

        log.info("Downloading pdf with Country :- {}", country);
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
