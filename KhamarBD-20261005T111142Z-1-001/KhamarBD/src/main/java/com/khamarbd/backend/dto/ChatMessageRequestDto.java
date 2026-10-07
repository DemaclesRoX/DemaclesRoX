package com.khamarbd.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChatMessageRequestDto {

    @NotNull(message = "consultationId is required")
    private Long consultationId;

    @NotNull(message = "senderId is required")
    private Long senderId;

    @NotBlank(message = "message content cannot be empty")
    private String content;

    public Long getConsultationId() { return consultationId; }
    public void setConsultationId(Long consultationId) { this.consultationId = consultationId; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
