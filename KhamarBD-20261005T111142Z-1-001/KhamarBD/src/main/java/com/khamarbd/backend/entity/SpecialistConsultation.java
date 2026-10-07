package com.khamarbd.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.ZonedDateTime;


@Entity
@Table(name = "specialist_consultation")
public class SpecialistConsultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultation_id")
    private Long consultationId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private User farmer;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "specialist_id")
    private User specialist;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "lot_id")
    private LotOrAnimal lotOrAnimal;

    @Column(name = "appointment_date", nullable = false)
    private ZonedDateTime appointmentDate;

    @Column(name = "status")
    private String status = "REQUESTED";

    @Column(name = "prescription_notes", columnDefinition = "TEXT")
    private String prescriptionNotes;

    @Column(name = "problem_description", columnDefinition = "TEXT")
    private String problemDescription;

    @Column(name = "fee_amount", precision = 10, scale = 2)
    private BigDecimal feeAmount;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @OneToOne(mappedBy = "consultation")
    private Prescription prescription;

    // Getters and Setters
    public Long getConsultationId() { return consultationId; }
    public void setConsultationId(Long consultationId) { this.consultationId = consultationId; }
    public User getFarmer() { return farmer; }
    public void setFarmer(User farmer) { this.farmer = farmer; }
    public User getSpecialist() { return specialist; }
    public void setSpecialist(User specialist) { this.specialist = specialist; }
    public LotOrAnimal getLotOrAnimal() { return lotOrAnimal; }
    public void setLotOrAnimal(LotOrAnimal lotOrAnimal) { this.lotOrAnimal = lotOrAnimal; }
    public ZonedDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(ZonedDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPrescriptionNotes() { return prescriptionNotes; }
    public void setPrescriptionNotes(String prescriptionNotes) { this.prescriptionNotes = prescriptionNotes; }
    public String getProblemDescription() { return problemDescription; }
    public void setProblemDescription(String problemDescription) { this.problemDescription = problemDescription; }
    public BigDecimal getFeeAmount() { return feeAmount; }
    public void setFeeAmount(BigDecimal feeAmount) { this.feeAmount = feeAmount; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
    public Prescription getPrescription() { return prescription; }
    public void setPrescription(Prescription prescription) { this.prescription = prescription; }

    public Long getFarmerId() {
        return farmer != null ? farmer.getUserId() : null;
    }

    public String getFarmerName() {
        return farmer != null ? farmer.getName() : null;
    }

    public String getFarmerPhone() {
        return farmer != null ? farmer.getPhone() : null;
    }

    public String getFarmerLocation() {
        if (farmer == null) return null;
        String upazila = farmer.getUpazila() != null ? farmer.getUpazila() : "";
        String district = farmer.getDistrict() != null ? farmer.getDistrict() : "";
        if (!upazila.isEmpty() && !district.isEmpty()) return upazila + ", " + district;
        return !upazila.isEmpty() ? upazila : district;
    }

    public Long getSpecialistId() {
        return specialist != null ? specialist.getUserId() : null;
    }

    public String getSpecialistName() {
        return specialist != null ? specialist.getName() : null;
    }

    public String getSpecialistPhone() {
        return specialist != null ? specialist.getPhone() : null;
    }

    public String getSpecialistLocation() {
        if (specialist == null) return null;
        String upazila = specialist.getUpazila() != null ? specialist.getUpazila() : "";
        String district = specialist.getDistrict() != null ? specialist.getDistrict() : "";
        if (!upazila.isEmpty() && !district.isEmpty()) return upazila + ", " + district;
        return !upazila.isEmpty() ? upazila : district;
    }

    public String getFarmerRole() {
        return farmer != null ? farmer.getPrimaryRole() : null;
    }

    public String getSpecialistRole() {
        return specialist != null ? specialist.getPrimaryRole() : null;
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

    public String getLotCategory() {
        return lotOrAnimal != null ? lotOrAnimal.getCategory() : null;
    }
}

