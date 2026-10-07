package com.khamarbd.backend.entity.support;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class WeatherRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sector;
    private String conditionParameter;
    private String operator;
    private Double thresholdValue; 
    private String alertMessage;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getConditionParameter() { return conditionParameter; }
    public void setConditionParameter(String conditionParameter) { this.conditionParameter = conditionParameter; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public Double getThresholdValue() { return thresholdValue; }
    public void setThresholdValue(Double thresholdValue) { this.thresholdValue = thresholdValue; }

    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }
}
