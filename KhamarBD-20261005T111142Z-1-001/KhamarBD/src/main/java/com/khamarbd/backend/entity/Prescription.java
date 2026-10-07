package com.khamarbd.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long prescriptionId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "consultation_id")
    private SpecialistConsultation consultation;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "specialist_id", nullable = false)
    private User specialist;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "lot_id")
    private LotOrAnimal lotOrAnimal;

    @Column(name = "diagnosis", nullable = false)
    private String diagnosis;

    @Column(name = "symptoms_note", columnDefinition = "TEXT")
    private String symptomsNote;

    @Column(name = "special_notice", columnDefinition = "TEXT")
    private String specialNotice;

    @Column(name = "template_code", length = 40)
    private String templateCode;

    @Column(name = "digital_signature")
    private String digitalSignature;

    @Column(name = "issued_at")
    private ZonedDateTime issuedAt = ZonedDateTime.now();

    @Column(name = "status", length = 20)
    private String status = "ISSUED"; // DRAFT, ISSUED

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionItem> items;

    // Constructors
    public Prescription() {
    }

    // Getters and Setters
    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public SpecialistConsultation getConsultation() {
        return consultation;
    }

    public void setConsultation(SpecialistConsultation consultation) {
        this.consultation = consultation;
    }

    public User getSpecialist() {
        return specialist;
    }

    public void setSpecialist(User specialist) {
        this.specialist = specialist;
    }

    public User getFarmer() {
        return farmer;
    }

    public void setFarmer(User farmer) {
        this.farmer = farmer;
    }

    public LotOrAnimal getLotOrAnimal() {
        return lotOrAnimal;
    }

    public void setLotOrAnimal(LotOrAnimal lotOrAnimal) {
        this.lotOrAnimal = lotOrAnimal;
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

    public ZonedDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(ZonedDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PrescriptionItem> getItems() {
        return items;
    }

    public void setItems(List<PrescriptionItem> items) {
        this.items = items;
    }

    public Long getConsultationId() {
        return consultation != null ? consultation.getConsultationId() : null;
    }

    public Long getFarmerId() {
        return farmer != null ? farmer.getUserId() : null;
    }

    public String getFarmerName() {
        return farmer != null ? farmer.getName() : null;
    }

    public String getFarmerPhone() {
        return farmer != null ? farmer.getPhone() : null;
    }

    public Long getSpecialistId() {
        return specialist != null ? specialist.getUserId() : null;
    }

    public String getSpecialistName() {
        return specialist != null ? specialist.getName() : null;
    }

    public String getSpecialistDegrees() {
        return specialist != null ? (specialist.getBio() != null ? specialist.getBio() : specialist.getSpecializationSector()) : null;
    }

    public String getSpecialistRegNo() {
        return specialist != null ? specialist.getRegistrationNo() : null;
    }

    public Long getLotId() {
        return lotOrAnimal != null ? lotOrAnimal.getLotId() : null;
    }

    public String getLotTag() {
        return lotOrAnimal != null ? lotOrAnimal.getIdentifierTag() : null;
    }

    public String getLotSpeciesOrBreed() {
        return lotOrAnimal != null ? lotOrAnimal.getSpeciesOrBreed() : null;
    }
}

