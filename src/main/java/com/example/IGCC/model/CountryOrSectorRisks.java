package com.example.IGCC.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@Document("CountryOrSectorRisks")
public class CountryOrSectorRisks {
    @Id
    @JsonProperty("No")
    private String id;
    @JsonProperty("CountryOrSectorRisks")
    private String country;
    @JsonProperty("TypeOfRisks")
    private String typeOfRisk;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("LevelOfRisks")
    private String levelOfRisk;
    @JsonProperty("Sources")
    private String source;
}
