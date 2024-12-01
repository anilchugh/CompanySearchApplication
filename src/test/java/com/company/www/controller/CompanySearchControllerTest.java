package com.company.www.controller;

import com.company.www.model.*;
import com.company.www.service.CompanySearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "apiKey=API_KEY")
@AutoConfigureMockMvc
class CompanySearchControllerTest {

    @MockBean
    private CompanySearchService companySearchService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        Mockito.when(companySearchService.searchCompanies(any(CompanySearch.class))).thenReturn(getCompanySearchResult());
        Mockito.when(companySearchService.searchOfficersByCompany("COMPANY_ID")).thenReturn(getOfficerSearchResult());
    }

    @Test
    void searchCompaniesWithoutApiToken() throws Exception {
        CompanySearch companySearch = new CompanySearch("COMPANY_NAME", "COMPANY_ID", true);
        mockMvc.perform(
                post("/www.company.com/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companySearch)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void searchCompaniesSucceedsWithApiToken() throws Exception {
        CompanySearch companySearch = new CompanySearch("COMPANY_NAME", "COMPANY_ID", true);
        mockMvc.perform(
                post("/www.company.com/search")
                        .header("x-api-key", "API_KEY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companySearch)))
                .andExpect(status().isOk());

    }

    @Test
    void searchActiveCompaniesSucceedsWithApiToken() throws Exception {
        CompanySearch companySearch = new CompanySearch("COMPANY_NAME", "COMPANY_ID", true);
        mockMvc.perform(
                post("/www.company.com/search")
                        .header("x-api-key", "API_KEY")
                        .param("isActive", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companySearch)))
                .andExpect(status().isOk());

    }

    @Test
    void searchCompaniesSucceedsWithMissingCompanyName() throws Exception {
        CompanySearch companySearch = new CompanySearch(null, "COMPANY_ID", true);
        String error = mockMvc.perform(
                post("/www.company.com/search")
                        .header("x-api-key", "API_KEY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companySearch)))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException().getMessage();
        Assertions.assertTrue(error.contains("Company name is mandatory"));

    }

    private CompanySearchResult getCompanySearchResult() {
        CompanySearchResult companySearchResult = new CompanySearchResult();
        companySearchResult.setCompanies(getCompanies());
        companySearchResult.setTotalResults(getCompanies().size());
        return companySearchResult;
    }

    private OfficerSearchResult getOfficerSearchResult() {
        OfficerSearchResult officerSearchResult = new OfficerSearchResult();
        officerSearchResult.setOfficers(getOfficers());
        officerSearchResult.setTotalResults(getOfficers().size());
        return officerSearchResult;
    }

    private List<Officer> getOfficers() {
        Officer officer = new Officer();
        officer.setAddress(getAddress());
        officer.setAppointedOn("2022-11-11");
        officer.setName("OFFICER_1_NAME");
        officer.setOfficerRole("Manager");
        return Arrays.asList(officer);
    }

    private List<Company> getCompanies() {
        Company company = new Company();
        company.setAddress(getAddress());
        company.setCompanyNumber("COMPANY_ID");
        company.setTitle("COMPANY_NAME");
        company.setDateOfCreation("2022-11-11");
        return Arrays.asList(company);
    }

    private Address getAddress() {
        Address address = new Address();
        address.setAddressLine1("ADDRESS_LINE_1");
        address.setCountry("UK");
        address.setPostalCode("EH12 9JQ");
        address.setLocality("Midlothian");
        return address;
    }

}


