package com.khamarbd.backend.dto;

import jakarta.validation.constraints.*;

public class LotOrAnimalRequestDto {

    @NotNull
    private Long farmId;

    @NotBlank
    @Size(min = 2, max = 20, message = "identifierTag must be 2-20 characters")
    private String identifierTag;

    @NotBlank
    @Pattern(regexp = "Individual|Lot", message = "category must be Individual or Lot")
    private String category;

    @NotBlank
    private String speciesOrBreed;

    @NotBlank
    private String startDate;

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    public String getIdentifierTag() {
        return identifierTag;
    }

    public void setIdentifierTag(String identifierTag) {
        this.identifierTag = identifierTag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSpeciesOrBreed() {
        return speciesOrBreed;
    }

    public void setSpeciesOrBreed(String speciesOrBreed) {
        this.speciesOrBreed = speciesOrBreed;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
