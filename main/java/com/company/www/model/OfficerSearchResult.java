package com.company.www.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OfficerSearchResult {
    @JsonProperty("items")
    List<Officer> officers;

    @JsonProperty("total_results")
    long totalResults = 0;

    public List<Officer> getOfficers() {
        return this.officers;
    }

    public void setOfficers(List<Officer> officers) {
        this.officers = officers;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public long getTotalResults() {
        return totalResults;
    }

}
