package com.company.www.model;

public enum CompanyStatus {
    ACTIVE("active"),
    DISSOLVED("dissolved");

    public final String status;

    private CompanyStatus(String status) {
        this.status = status;
    }

}
