package com.khamarbd.backend.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class RoutineTaskRequestDto {

    @NotNull
    private Long lotId;

    @NotBlank
    private String taskTitle;

    @NotBlank
    private String actionType;

    @NotNull
    private LocalDate dueDate;

    private boolean costPrompt;

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCostPrompt() {
        return costPrompt;
    }

    public void setCostPrompt(boolean costPrompt) {
        this.costPrompt = costPrompt;
    }
}
