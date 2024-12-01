package com.company.www.service;

import com.company.www.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompanySearchService {

    private static final String X_API_KEY_NAME = "x-api-key";

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public CompanySearchResult searchCompanies(CompanySearch companySearch) {

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(getHttpHeaders());
        ResponseEntity<CompanySearchResult> response = restTemplateBuilder.build().exchange("https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Search?Query=companies", HttpMethod.GET, request, CompanySearchResult.class);
        if (response.hasBody() && response.getBody().getTotalResults() > 0) {
            //filter companies based on companyId or companyName match and also active status
            List<Company> filteredCompanies = response.getBody().getCompanies().stream().
                    filter(c -> StringUtils.hasText(companySearch.getCompanyNumber()) ? companySearch.getCompanyNumber().equals(c.getCompanyNumber()) : c.getTitle().equals(companySearch.getCompanyName()))
                    .filter(c -> companySearch.isActive() ? CompanyStatus.ACTIVE.status.equals(c.getCompanyStatus()) : true)
                    .collect(Collectors.toUnmodifiableList());
            response.getBody().setCompanies(filteredCompanies);
            response.getBody().setTotalResults(filteredCompanies.size());
        }
        return response.getBody();
    }

    public OfficerSearchResult searchOfficersByCompany(String companyNumber) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(getHttpHeaders());
        Map<String, String> params = new HashMap<String, String>();
        params.put("CompanyNumber", companyNumber);
        ResponseEntity<OfficerSearchResult> response = restTemplateBuilder.build().exchange("https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber={CompanyNumber}", HttpMethod.GET, request, OfficerSearchResult.class, params);
        if (response.hasBody() && response.getBody().getTotalResults() > 0) {
            //filter out officers that have resigned/not active
            List<Officer> filteredOfficers = response.getBody().getOfficers().stream().
                    filter(o -> !StringUtils.hasText(o.getResignedOn())).collect(Collectors.toUnmodifiableList());
            response.getBody().setOfficers(filteredOfficers);
            response.getBody().setTotalResults(filteredOfficers.size());
        }
        return response.getBody();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
        headers.add(X_API_KEY_NAME, String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        return headers;
    }

}
