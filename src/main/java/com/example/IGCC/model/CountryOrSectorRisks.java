package com.example.IGCC.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document("CountryOrSectorRisks")
public class CountryOrSectorRisks {
    @Id
    private String id;
    private String country;
    private String typeOfRisk;
    private String description;
    private String levelOfRisk;
    private String source;
}
