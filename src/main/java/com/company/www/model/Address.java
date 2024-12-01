package com.company.www.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @JsonProperty
    String locality;

    @JsonProperty("postal_code")
    String postalCode;

    @JsonProperty
    String premises;

    @JsonProperty("address_line_1")
    String addressLine1;

    @JsonProperty
    String country;
}
