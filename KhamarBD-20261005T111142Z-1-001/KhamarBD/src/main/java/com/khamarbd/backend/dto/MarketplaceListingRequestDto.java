package com.khamarbd.backend.dto;

import jakarta.validation.constraints.*;

public class MarketplaceListingRequestDto {

    @NotNull
    private Long sellerId;

    private Long lotId; // optional — not every listing traces back to a lot

    @NotBlank
    @Size(min = 4, max = 160, message = "title must be 4-160 characters")
    private String title;

    @NotBlank
    private String productCategory;

    @Positive(message = "price must be greater than 0")
    private double price;

    @Min(value = 1, message = "quantityAvailable must be at least 1")
    private int quantityAvailable;

    @NotBlank
    private String unit;

    private String brandName;
    private String farmSector;
    private String description;
    private String imageUrls;
    private String division;
    private String district;
    private String upazila;

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getFarmSector() { return farmSector; }
    public void setFarmSector(String farmSector) { this.farmSector = farmSector; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrls() { return imageUrls; }
    public void setImageUrls(String imageUrls) { this.imageUrls = imageUrls; }

    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getUpazila() { return upazila; }
    public void setUpazila(String upazila) { this.upazila = upazila; }
}
