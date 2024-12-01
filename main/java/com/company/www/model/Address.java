package com.company.www.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPremises() {
        return premises;
    }

    public void setPremises(String premises) {
        this.premises = premises;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
