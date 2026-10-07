package com.khamarbd.backend.entity.support;

import jakarta.persistence.*;

/**
 * Supporting Reference Table (Section 5.3)
 * Stores all dropdown / autocomplete terms (Section 3 of domain seed and supplier seed).
 */
@Entity
@Table(name = "taxonomy_item")
public class TaxonomyItem {

    @Id
    @Column(name = "item_code", length = 60, nullable = false)
    private String itemCode;

    @Column(name = "domain", length = 40, nullable = false)
    private String domain; // INPUT, TASK, BRAND, UNIT, FEED, LEDGER_CATEGORY, PRODUCT_CATEGORY

    @Column(name = "category_code", length = 60)
    private String categoryCode;

    @Column(name = "sector", length = 60)
    private String sector; // POULTRY, FISHERIES, LIVESTOCK, AGRICULTURE, or NULL for cross-sector

    @Column(name = "name_bn", length = 150)
    private String nameBn;

    @Column(name = "name_en", length = 150)
    private String nameEn;

    @Column(name = "aliases", length = 255)
    private String aliases;

    @Column(name = "parent_code", length = 60)
    private String parentCode;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "seed_version", length = 20)
    private String seedVersion;

    // Constructors
    public TaxonomyItem() {
    }

    public TaxonomyItem(String itemCode, String domain, String categoryCode, String sector, String nameBn, String nameEn, String aliases, String parentCode, Boolean isActive, String seedVersion) {
        this.itemCode = itemCode;
        this.domain = domain;
        this.categoryCode = categoryCode;
        this.sector = sector;
        this.nameBn = nameBn;
        this.nameEn = nameEn;
        this.aliases = aliases;
        this.parentCode = parentCode;
        this.isActive = isActive;
        this.seedVersion = seedVersion;
    }

    // Getters and Setters
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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

    public String getAliases() {
        return aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getSeedVersion() {
        return seedVersion;
    }

    public void setSeedVersion(String seedVersion) {
        this.seedVersion = seedVersion;
    }
}
