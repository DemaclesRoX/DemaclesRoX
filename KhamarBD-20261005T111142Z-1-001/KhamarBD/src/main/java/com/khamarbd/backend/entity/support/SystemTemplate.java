package com.khamarbd.backend.entity.support;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

/**
 * Supporting Reference Table (Section 5.3)
 * Stores built-in routine templates and prescription preset templates.
 */
@Entity
@Table(name = "system_template")
public class SystemTemplate {

    @Id
    @Column(name = "template_code", length = 50, nullable = false)
    private String templateCode;

    @Column(name = "template_type", length = 30, nullable = false)
    private String templateType; // ROUTINE, PRESCRIPTION

    @Column(name = "sector", length = 30, nullable = false)
    private String sector; // POULTRY, FISHERIES, LIVESTOCK, AGRICULTURE

    @Column(name = "variety", length = 60)
    private String variety;

    @Column(name = "name_bn", length = 120)
    private String nameBn;

    @Column(name = "name_en", length = 120)
    private String nameEn;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "owner_user_id")
    private Long ownerUserId; // NULL = system preset, set = custom template

    @Column(name = "content_json", columnDefinition = "TEXT")
    private String contentJson; // JSON array of tasks or prescription lines

    @Column(name = "seed_version", length = 20)
    private String seedVersion;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    // Constructors
    public SystemTemplate() {
    }

    public SystemTemplate(String templateCode, String templateType, String sector, String variety, String nameBn, String nameEn, Integer durationDays, String contentJson, String seedVersion) {
        this.templateCode = templateCode;
        this.templateType = templateType;
        this.sector = sector;
        this.variety = variety;
        this.nameBn = nameBn;
        this.nameEn = nameEn;
        this.durationDays = durationDays;
        this.contentJson = contentJson;
        this.seedVersion = seedVersion;
        this.createdAt = ZonedDateTime.now();
    }

    // Getters and Setters
    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

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

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
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

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getContentJson() {
        return contentJson;
    }

    public void setContentJson(String contentJson) {
        this.contentJson = contentJson;
    }

    public String getSeedVersion() {
        return seedVersion;
    }

    public void setSeedVersion(String seedVersion) {
        this.seedVersion = seedVersion;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
