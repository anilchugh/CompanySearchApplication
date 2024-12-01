package com.company.www.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@RequiredArgsConstructor
public class Company {
    @JsonProperty("company_number")
    private String companyNumber;

    @JsonProperty("company_type")
    private String companyType;

    @JsonProperty
    private String title;

    @JsonProperty("company_status")
    private String companyStatus;

    @JsonProperty("date_of_creation")
    private String dateOfCreation;

    @JsonProperty
    private Address address;

    @JsonProperty
    private List<Officer> officers;

}
