package com.company.www.controller;

import com.company.www.model.Company;
import com.company.www.model.CompanySearch;
import com.company.www.model.CompanySearchResult;
import com.company.www.model.OfficerSearchResult;
import com.company.www.service.CompanySearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/www.company.com")
public class CompanySearchController {

    @Autowired
    private CompanySearchService companySearchService;

    @PostMapping("/search")
    public ResponseEntity<List<Company>> searchCompanies(@Valid @RequestBody CompanySearch companySearch, @RequestParam(required = false) boolean isActive) {
        companySearch.setActive(isActive);
        //perform company search
        CompanySearchResult companySearchResult = companySearchService.searchCompanies(companySearch);
        if (companySearchResult != null && companySearchResult.getTotalResults() > 0) {
            for (Company company : companySearchResult.getCompanies()) {
                //retrieve officers in company
                OfficerSearchResult officerSearchResult = companySearchService.searchOfficersByCompany(company.getCompanyNumber());
                if (officerSearchResult != null && officerSearchResult.getTotalResults() > 0) {
                    company.setOfficers(officerSearchResult.getOfficers());
                }
            }
            return new ResponseEntity(companySearchResult, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
