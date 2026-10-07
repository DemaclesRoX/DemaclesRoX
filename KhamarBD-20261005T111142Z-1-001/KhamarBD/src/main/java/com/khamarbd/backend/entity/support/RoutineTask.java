package com.khamarbd.backend.entity.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.khamarbd.backend.entity.LotOrAnimal;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Supporting Reference / Operational Table (Section 5.3)
 * Stores scheduled dated routine tasks generated for a tracking unit (LotOrAnimal).
 */
@Entity
@Table(name = "routine_task")
public class RoutineTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "lot_id", nullable = false)
    private LotOrAnimal lotOrAnimal;

    @Column(name = "source_template_code", length = 50)
    private String sourceTemplateCode;

    @Column(name = "day_offset")
    private Integer dayOffset;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "title_bn", length = 200)
    private String titleBn;

    @Column(name = "title_en", length = 200)
    private String titleEn;

    @Column(name = "action_type_code", length = 50)
    private String actionTypeCode;

    @Column(name = "frequency", length = 30)
    private String frequency;

    @Column(name = "status", length = 20)
    private String status = "PENDING"; // PENDING, DONE, SKIPPED

    @Column(name = "done_at")
    private ZonedDateTime doneAt;

    @Column(name = "done_by")
    private Long doneBy;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "cost_prompt")
    private Boolean costPrompt = false;

    @Column(name = "suggested_ledger_category", length = 60)
    private String suggestedLedgerCategory;

    // Constructors
    public RoutineTask() {
    }

    // Getters and Setters
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public LotOrAnimal getLotOrAnimal() {
        return lotOrAnimal;
    }

    public Long getLotId() {
        return lotOrAnimal != null ? lotOrAnimal.getLotId() : null;
    }

    public void setLotOrAnimal(LotOrAnimal lotOrAnimal) {
        this.lotOrAnimal = lotOrAnimal;
    }

    public String getSourceTemplateCode() {
        return sourceTemplateCode;
    }

    public void setSourceTemplateCode(String sourceTemplateCode) {
        this.sourceTemplateCode = sourceTemplateCode;
    }

    public Integer getDayOffset() {
        return dayOffset;
    }

    public void setDayOffset(Integer dayOffset) {
        this.dayOffset = dayOffset;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getTitleBn() {
        return titleBn;
    }

    public void setTitleBn(String titleBn) {
        this.titleBn = titleBn;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getActionTypeCode() {
        return actionTypeCode;
    }

    public void setActionTypeCode(String actionTypeCode) {
        this.actionTypeCode = actionTypeCode;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(ZonedDateTime doneAt) {
        this.doneAt = doneAt;
    }

    public Long getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(Long doneBy) {
        this.doneBy = doneBy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getCostPrompt() {
        return costPrompt;
    }

    public void setCostPrompt(Boolean costPrompt) {
        this.costPrompt = costPrompt;
    }

    public String getSuggestedLedgerCategory() {
        return suggestedLedgerCategory;
    }

    public void setSuggestedLedgerCategory(String suggestedLedgerCategory) {
        this.suggestedLedgerCategory = suggestedLedgerCategory;
    }
}
