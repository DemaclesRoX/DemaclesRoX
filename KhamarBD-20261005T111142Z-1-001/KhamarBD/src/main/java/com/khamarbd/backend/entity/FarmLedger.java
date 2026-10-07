package com.khamarbd.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "farm_ledger")
public class FarmLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ledger_id")
    private Long ledgerId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "lot_id")
    private LotOrAnimal lotOrAnimal;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @Column(name = "entry_type", nullable = false)
    private String entryType;

    @Column(name = "category", nullable = false, length = 40)
    private String category;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "routine_task_id")
    private Long routineTaskId;

    @Column(name = "prescription_id")
    private Long prescriptionId;

    @Column(name = "quantity", precision = 12, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_code", length = 20)
    private String unitCode;

    @Column(name = "details_json", columnDefinition = "JSON")
    private String detailsJson;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    // Getters and Setters
    public Long getLedgerId() { return ledgerId; }
    public void setLedgerId(Long ledgerId) { this.ledgerId = ledgerId; }
    public LotOrAnimal getLotOrAnimal() { return lotOrAnimal; }
    public void setLotOrAnimal(LotOrAnimal lotOrAnimal) { this.lotOrAnimal = lotOrAnimal; }
    public String getEntryType() { return entryType; }
    public void setEntryType(String entryType) { this.entryType = entryType; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getRoutineTaskId() { return routineTaskId; }
    public void setRoutineTaskId(Long routineTaskId) { this.routineTaskId = routineTaskId; }
    public Long getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(Long prescriptionId) { this.prescriptionId = prescriptionId; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getUnitCode() { return unitCode; }
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }
    public String getDetailsJson() { return detailsJson; }
    public void setDetailsJson(String detailsJson) { this.detailsJson = detailsJson; }
    public Farm getFarm() { return farm; }
    public void setFarm(Farm farm) { this.farm = farm; }
    public String getCategoryCode() { return category; }
    public void setCategoryCode(String categoryCode) { this.category = categoryCode; }
    public String getNote() { return notes; }
    public void setNote(String note) { this.notes = note; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    // Helper getters for JSON serialization
    public Long getLotId() { return lotOrAnimal != null ? lotOrAnimal.getLotId() : null; }
    public String getLotTag() { return lotOrAnimal != null ? lotOrAnimal.getIdentifierTag() : null; }
    public String getLotCategory() { return lotOrAnimal != null ? lotOrAnimal.getCategory() : null; }
    public Long getFarmId() { return farm != null ? farm.getFarmId() : null; }
    public String getFarmName() { return farm != null ? farm.getFarmName() : null; }
}
