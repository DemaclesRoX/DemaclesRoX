package com.khamarbd.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.khamarbd.backend.entity.support.RoutineTask;

@Entity
@Table(name = "lot_or_animal")
public class LotOrAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lot_id")
    private Long lotId;

    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @Column(name = "identifier_tag", nullable = false, length = 60)
    private String identifierTag;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "species_or_breed", nullable = false, length = 80)
    private String speciesOrBreed;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "status")
    private String status = "ACTIVE";

    @Column(name = "quantity")
    private Integer quantity = 1;

    @Column(name = "expected_harvest_date")
    private LocalDate expectedHarvestDate;

    @Column(name = "unit_kind", nullable = false)
    private String unitKind;

    @Column(name = "area_size", precision = 10, scale = 2)
    private BigDecimal areaSize;

    @Column(name = "area_unit")
    private String areaUnit;

    @Column(name = "routine_template_code", length = 40)
    private String routineTemplateCode;

    @Column(name = "closed_at")
    private LocalDate closedAt;

    @Column(name = "closing_note", columnDefinition = "TEXT")
    private String closingNote;

    @JsonIgnore
    @OneToMany(mappedBy = "lotOrAnimal")
    private List<FarmLedger> ledgers;

    @JsonIgnore
    @OneToMany(mappedBy = "lotOrAnimal")
    private List<RoutineTask> tasks;

    // Getters and Setters
    public Long getLotId() { return lotId; }
    public void setLotId(Long lotId) { this.lotId = lotId; }
    public Farm getFarm() { return farm; }
    public void setFarm(Farm farm) { this.farm = farm; }
    public List<RoutineTask> getTasks() { return tasks; }
    public void setTasks(List<RoutineTask> tasks) { this.tasks = tasks; }
    public String getIdentifierTag() { return identifierTag; }
    public void setIdentifierTag(String identifierTag) { this.identifierTag = identifierTag; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSpeciesOrBreed() { return speciesOrBreed; }
    public void setSpeciesOrBreed(String speciesOrBreed) { this.speciesOrBreed = speciesOrBreed; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDate getExpectedHarvestDate() { return expectedHarvestDate; }
    public void setExpectedHarvestDate(LocalDate expectedHarvestDate) { this.expectedHarvestDate = expectedHarvestDate; }
    public String getUnitKind() { return unitKind; }
    public void setUnitKind(String unitKind) { this.unitKind = unitKind; }
    public BigDecimal getAreaSize() { return areaSize; }
    public void setAreaSize(BigDecimal areaSize) { this.areaSize = areaSize; }
    public String getAreaUnit() { return areaUnit; }
    public void setAreaUnit(String areaUnit) { this.areaUnit = areaUnit; }
    public String getRoutineTemplateCode() { return routineTemplateCode; }
    public void setRoutineTemplateCode(String routineTemplateCode) { this.routineTemplateCode = routineTemplateCode; }
    public LocalDate getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDate closedAt) { this.closedAt = closedAt; }
    public String getClosingNote() { return closingNote; }
    public void setClosingNote(String closingNote) { this.closingNote = closingNote; }
    public List<FarmLedger> getLedgers() { return ledgers; }
    public void setLedgers(List<FarmLedger> ledgers) { this.ledgers = ledgers; }
}

