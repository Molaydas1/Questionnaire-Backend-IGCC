package com.example.IGCC.service;

import com.example.IGCC.exception.NoRecordsFoundExcption;
import com.example.IGCC.model.CountryOrSectorRisks;
import com.example.IGCC.repository.CountryOrSectorRisksRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CountryOrSectorRisksService {
    @Autowired
    private CountryOrSectorRisksRepository countryOrSectorRisksRepository;
    @Autowired
    private TemplateEngine templateEngine;

    // Load Excel file
    public void importDataFromExcel(MultipartFile file) throws IOException {
        List<List<String>> rows = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        rows = StreamSupport.stream(sheet.spliterator(), false)
                .map(row -> StreamSupport
                        .stream(row.spliterator(), false)
                        .map(this::getCellStringValue)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        // Save data to the database

        List<CountryOrSectorRisks> excelDataList = rows.stream().skip(2).map(row -> {
            CountryOrSectorRisks excelData = new CountryOrSectorRisks();
            excelData.setId(row.get(0));
            excelData.setCountry(row.get(1));
            excelData.setTypeOfRisk(row.get(2));
            excelData.setDescription(row.get(3));
            excelData.setLevelOfRisk(row.get(4));
            excelData.setSource(row.get(5));
            return excelData;
        }).collect(Collectors.toList());
        countryOrSectorRisksRepository.saveAll(excelDataList);
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
    public byte[] generatePdf(List<CountryOrSectorRisks> countryOrSectorRisks) throws Exception {

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
