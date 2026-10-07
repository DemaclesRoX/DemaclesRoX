package com.khamarbd.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class TaxonomyItemRequestDto {

    @NotBlank
    private String domain;

    @NotBlank
    private String sector;

    @NotBlank
    private String categoryCode;

    @NotBlank
    private String nameBn;

    @NotBlank
    private String nameEn;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getNameBn() {
        return nameBn;
    }

    public void setNameBn(String nameBn) {
        this.nameBn = nameBn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
