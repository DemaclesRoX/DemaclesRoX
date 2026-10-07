package com.khamarbd.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WeatherRuleRequestDto {

    @NotBlank
    private String sector;

    @NotBlank
    private String conditionParameter;

    @NotBlank
    private String operator;

    @NotNull
    private Double thresholdValue;

    @NotBlank
    private String alertMessage;

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getConditionParameter() {
        return conditionParameter;
    }

    public void setConditionParameter(String conditionParameter) {
        this.conditionParameter = conditionParameter;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(Double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }
}
