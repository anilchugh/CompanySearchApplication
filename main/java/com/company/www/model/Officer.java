package com.company.www.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Officer {
    @JsonProperty
    private String name;
    @JsonProperty("officer_role")
    private String officerRole;
    @JsonProperty("appointed_on")
    private String appointedOn;
    @JsonProperty("resigned_on")
    private String resignedOn;
    @JsonProperty
    private Address address;

    public void setName(String name) {
        this.name = name;
    }

    public void setOfficerRole(String officerRole) {
        this.officerRole = officerRole;
    }

    public void setAppointedOn(String appointedOn) {
        this.appointedOn = appointedOn;
    }

    public void setResignedOn(String resignedOn) {
        this.resignedOn = resignedOn;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAppointedOn() {
        return appointedOn;
    }

    public String getName() {
        return name;
    }

    public String getOfficerRole() {
        return officerRole;
    }

    public String getResignedOn() {
        return resignedOn;
    }

    public Address getAddress() {
        return address;
    }

}
