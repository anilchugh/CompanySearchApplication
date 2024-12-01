package com.company.www.service;

import com.company.www.config.ApiKeyAuthentication;
import com.company.www.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(properties = "apiKey=API_KEY")
@AutoConfigureMockMvc
class CompanySearchServiceTest {

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseEntity<CompanySearchResult> companySearchResponseEntity;

    @Mock
    private ResponseEntity<OfficerSearchResult> officerSearchResponseEntity;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    CompanySearchService companySearchService;

    @BeforeEach
    void setup() {
        SecurityContextHolder.getContext().setAuthentication(new ApiKeyAuthentication("API_KEY", AuthorityUtils.NO_AUTHORITIES));
        Mockito.when(restTemplateBuilder.build()).thenReturn(restTemplate);
        Mockito.when(restTemplate.exchange(eq("https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Search?Query=companies"), eq(HttpMethod.GET), any(HttpEntity.class), eq(CompanySearchResult.class))).thenReturn(companySearchResponseEntity);
        Mockito.when(restTemplate.exchange(eq("https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber={CompanyNumber}"), eq(HttpMethod.GET), any(HttpEntity.class), eq(OfficerSearchResult.class), any(Map.class))).thenReturn(officerSearchResponseEntity);
        Mockito.when(companySearchResponseEntity.hasBody()).thenReturn(true);
        Mockito.when(companySearchResponseEntity.getBody()).thenReturn(getCompanySearchResult());
        Mockito.when(officerSearchResponseEntity.hasBody()).thenReturn(true);
        Mockito.when(officerSearchResponseEntity.getBody()).thenReturn(getOfficerSearchResult());
    }

    @Test
    void searchCompaniesWithCompanyNameAndNumberSuccess() throws Exception {
        CompanySearch companySearch = new CompanySearch("COMPANY_NAME_2", "COMPANY_ID_2");
        CompanySearchResult companySearchResult = companySearchService.searchCompanies(companySearch);
        Assertions.assertEquals(1, companySearchResult.getTotalResults());
        Assertions.assertEquals("COMPANY_ID_2", companySearchResult.getCompanies().get(0).getCompanyNumber());
        Assertions.assertEquals("COMPANY_NAME_2", companySearchResult.getCompanies().get(0).getTitle());
        Assertions.assertEquals("2010-10-10", companySearchResult.getCompanies().get(0).getDateOfCreation());
        Assertions.assertEquals(CompanyStatus.ACTIVE.status, companySearchResult.getCompanies().get(0).getCompanyStatus());
    }

    @Test
    void searchOfficersByCompanySuccess() throws Exception {
        OfficerSearchResult officerSearchResult = companySearchService.searchOfficersByCompany("COMPANY_ID_2");
        Assertions.assertEquals(1, officerSearchResult.getTotalResults());
        Assertions.assertEquals("OFFICER_1_NAME", officerSearchResult.getOfficers().get(0).getName());
        Assertions.assertEquals("Manager", officerSearchResult.getOfficers().get(0).getOfficerRole());
        Assertions.assertEquals("2022-11-11", officerSearchResult.getOfficers().get(0).getAppointedOn());
    }

    @Test
    void searchOfficersByCompanyWithNoMatchFound() throws Exception {
        Mockito.when(officerSearchResponseEntity.hasBody()).thenReturn(false);
        Mockito.when(officerSearchResponseEntity.getBody()).thenReturn(null);
        Assertions.assertNull(companySearchService.searchOfficersByCompany("COMPANY_ID_3"));
    }

    @Test
    void searchCompaniesWithCompanyNameOnlySuccess() throws Exception {
        CompanySearch companySearch = new CompanySearch("COMPANY_NAME_1", null);
        CompanySearchResult companySearchResult = companySearchService.searchCompanies(companySearch);
        Assertions.assertEquals(1, companySearchResult.getTotalResults());
        Assertions.assertEquals("COMPANY_ID_1", companySearchResult.getCompanies().get(0).getCompanyNumber());
        Assertions.assertEquals("COMPANY_NAME_1", companySearchResult.getCompanies().get(0).getTitle());
        Assertions.assertEquals("2020-20-20", companySearchResult.getCompanies().get(0).getDateOfCreation());
        Assertions.assertEquals(CompanyStatus.ACTIVE.status, companySearchResult.getCompanies().get(0).getCompanyStatus());
    }

    @Test
    void searchCompaniesWithNoMatchFound() throws Exception {
        Mockito.when(companySearchResponseEntity.hasBody()).thenReturn(false);
        Mockito.when(companySearchResponseEntity.getBody()).thenReturn(null);
        CompanySearch companySearch = new CompanySearch("COMPANY_NAME_0", null);
        Assertions.assertNull(companySearchService.searchCompanies(companySearch));
    }

    @Test
    void searchForNonActiveCompanyWithMatchFound() throws Exception {
        CompanySearch companySearch = new CompanySearch("COMPANY_NAME_3", "COMPANY_ID_3");
        companySearch.setActive(false);
        CompanySearchResult companySearchResult = companySearchService.searchCompanies(companySearch);
        Assertions.assertEquals(1, companySearchResult.getTotalResults());
        Assertions.assertEquals("COMPANY_ID_3", companySearchResult.getCompanies().get(0).getCompanyNumber());
        Assertions.assertEquals("COMPANY_NAME_3", companySearchResult.getCompanies().get(0).getTitle());
        Assertions.assertEquals("2011-11-11", companySearchResult.getCompanies().get(0).getDateOfCreation());
        Assertions.assertEquals(CompanyStatus.DISSOLVED.status, companySearchResult.getCompanies().get(0).getCompanyStatus());
    }

    @Test
    void searchForActiveCompanyWithNoMatchFound() throws Exception {
        CompanySearch companySearch = new CompanySearch("COMPANY_NAME_3", "COMPANY_ID_3");
        companySearch.setActive(true);
        CompanySearchResult companySearchResult = companySearchService.searchCompanies(companySearch);
        Assertions.assertNotNull(companySearchResult);
        Assertions.assertEquals(0, companySearchResult.getTotalResults());
        Assertions.assertTrue(CollectionUtils.isEmpty(companySearchResult.getCompanies()));
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
        Officer officer1 = new Officer();
        officer1.setAddress(getAddress());
        officer1.setAppointedOn("2022-11-11");
        officer1.setName("OFFICER_1_NAME");
        officer1.setOfficerRole("Manager");

        Officer officer2 = new Officer();
        officer2.setAddress(getAddress());
        officer2.setAppointedOn("2012-12-12");
        officer2.setResignedOn("2015-12-12");
        officer2.setName("OFFICER_2_NAME");
        officer2.setOfficerRole("Senior Manager");
        return Arrays.asList(officer1, officer2);
    }


    private List<Company> getCompanies() {
        Company company1 = new Company();
        company1.setAddress(getAddress());
        company1.setCompanyStatus(CompanyStatus.ACTIVE.status);
        company1.setCompanyNumber("COMPANY_ID_1");
        company1.setTitle("COMPANY_NAME_1");
        company1.setDateOfCreation("2020-20-20");

        Company company2 = new Company();
        company2.setAddress(getAddress());
        company2.setCompanyStatus(CompanyStatus.ACTIVE.status);
        company2.setCompanyNumber("COMPANY_ID_2");
        company2.setTitle("COMPANY_NAME_2");
        company2.setDateOfCreation("2010-10-10");

        Company company3 = new Company();
        company3.setAddress(getAddress());
        company3.setCompanyStatus(CompanyStatus.DISSOLVED.status);
        company3.setCompanyNumber("COMPANY_ID_3");
        company3.setTitle("COMPANY_NAME_3");
        company3.setDateOfCreation("2011-11-11");
        return Arrays.asList(company1, company2, company3);
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


