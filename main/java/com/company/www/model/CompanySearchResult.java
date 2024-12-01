package com.company.www.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CompanySearchResult {
    @JsonProperty("items")
    List<Company> companies;

    @JsonProperty("total_results")
    long totalResults = 0;

    public List<Company> getCompanies() {
        return this.companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public long getTotalResults() {
        return totalResults;
    }

}
