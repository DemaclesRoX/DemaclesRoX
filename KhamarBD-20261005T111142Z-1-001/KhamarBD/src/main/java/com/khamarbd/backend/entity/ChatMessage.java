package com.khamarbd.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private SpecialistConsultation consultation;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "sent_at")
    private ZonedDateTime sentAt = ZonedDateTime.now();

    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }
    public SpecialistConsultation getConsultation() { return consultation; }
    public void setConsultation(SpecialistConsultation consultation) { this.consultation = consultation; }
    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public ZonedDateTime getSentAt() { return sentAt; }
    public void setSentAt(ZonedDateTime sentAt) { this.sentAt = sentAt; }

    // Exposed in JSON (relations are @JsonIgnore'd to avoid recursion)
    public Long getConsultationId() { return consultation != null ? consultation.getConsultationId() : null; }
    public Long getSenderId() { return sender != null ? sender.getUserId() : null; }
    public String getSenderName() { return sender != null ? sender.getName() : null; }
}
