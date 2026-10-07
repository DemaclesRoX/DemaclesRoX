package com.khamarbd.backend.dto;

import java.util.List;

public class PrescriptionRequestDto {

    private Long consultationId;
    private Long farmerId;
    private Long specialistId;
    private Long lotId;
    private String diagnosis;
    private String symptomsNote;
    private String specialNotice;
    private String templateCode;
    private String digitalSignature;
    private String issuedDate;
    private String notes;
    private List<PrescriptionItemRequestDto> items;

    public Long getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Long consultationId) {
        this.consultationId = consultationId;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public Long getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(Long specialistId) {
        this.specialistId = specialistId;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSymptomsNote() {
        return symptomsNote;
    }

    public void setSymptomsNote(String symptomsNote) {
        this.symptomsNote = symptomsNote;
    }

    public String getSpecialNotice() {
        return specialNotice;
    }

    public void setSpecialNotice(String specialNotice) {
        this.specialNotice = specialNotice;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<PrescriptionItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<PrescriptionItemRequestDto> items) {
        this.items = items;
    }
}
