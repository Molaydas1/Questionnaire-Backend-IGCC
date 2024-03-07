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

    // Load Excel file
//    public void importDataFromExcel(MultipartFile file) throws IOException {
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
//        // Save data to the database
//
//        List<CountryOrSectorRisks> excelDataList = rows.stream().skip(2).map(row -> {
//            CountryOrSectorRisks excelData = new CountryOrSectorRisks();
//            excelData.setId(row.get(0));
//            excelData.setCountry(row.get(1));
//            excelData.setTypeOfRisk(row.get(2));
//            excelData.setDescription(row.get(3));
//            excelData.setLevelOfRisk(row.get(4));
//            excelData.setSource(row.get(5));
//            return excelData;
//        }).collect(Collectors.toList());
//        countryOrSectorRisksRepository.saveAll(excelDataList);
//    }
//
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
