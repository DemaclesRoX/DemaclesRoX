package com.khamarbd.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class SystemTemplateRequestDto {

    @NotBlank
    private String templateType; // e.g. ROUTINE, PRESCRIPTION

    @NotBlank
    private String sector;

    @NotBlank
    private String templateName;

    private String contentJson;

    private Long ownerUserId;

    public Long getOwnerUserId() { return ownerUserId; }

    public void setOwnerUserId(Long ownerUserId) { this.ownerUserId = ownerUserId; }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getContentJson() {
        return contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }
}

