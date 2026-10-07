package com.khamarbd.backend.dto;

import jakarta.validation.constraints.*;

public class FarmRequestDto {

    @NotNull
    private Long userId;

    @NotBlank
    @Size(min = 3, max = 50, message = "Farm name must be 3-50 characters")
    private String farmName;

    @NotBlank
    @Pattern(regexp = "POULTRY|FISHERIES|DAIRY_LIVESTOCK|AGRICULTURE", message = "farmType must be POULTRY, FISHERIES, DAIRY_LIVESTOCK or AGRICULTURE")
    private String farmType;

    @NotBlank
    private String locationDetails;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getFarmType() {
        return farmType;
    }

    public void setFarmType(String farmType) {
        this.farmType = farmType;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }
}
