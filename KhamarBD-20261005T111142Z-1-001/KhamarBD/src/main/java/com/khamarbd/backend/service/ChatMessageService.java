package com.khamarbd.backend.service;

import com.khamarbd.backend.dto.ChatMessageRequestDto;
import com.khamarbd.backend.entity.ChatMessage;
import com.khamarbd.backend.entity.SpecialistConsultation;
import com.khamarbd.backend.entity.User;
import com.khamarbd.backend.repository.ChatMessageRepository;
import com.khamarbd.backend.repository.SpecialistConsultationRepository;
import com.khamarbd.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private SpecialistConsultationRepository consultationRepository;

    @Autowired
    private UserRepository userRepository;

    public ChatMessage sendMessage(ChatMessageRequestDto dto) {
        SpecialistConsultation consultation = consultationRepository.findById(dto.getConsultationId())
                .orElseThrow(() -> new IllegalArgumentException("Consultation not found"));

        // Chat opens for ACCEPTED, PRESCRIBED, ACTIVE, OPEN, REQUESTED, or general direct conversations
        String status = consultation.getStatus();
        if ("CANCELLED".equalsIgnoreCase(status) || "REJECTED".equalsIgnoreCase(status)) {
            throw new IllegalStateException("Cannot send message in a cancelled or rejected conversation");
        }

        // Only participants of THIS conversation may send messages
        Long senderId = dto.getSenderId();
        boolean isParticipant = (consultation.getFarmerId() != null && senderId.equals(consultation.getFarmerId()))
                || (consultation.getSpecialistId() != null && senderId.equals(consultation.getSpecialistId()));
        if (!isParticipant) {
            throw new IllegalStateException("You are not a participant of this conversation");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        ChatMessage message = new ChatMessage();
        message.setConsultation(consultation);
        message.setSender(sender);
        message.setContent(dto.getContent().trim());
        message.setSentAt(java.time.ZonedDateTime.now());

        // Bump consultation updatedAt so active threads rise to top
        consultation.setUpdatedAt(java.time.ZonedDateTime.now());
        consultationRepository.save(consultation);

        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessages(Long consultationId, Long afterId) {
        if (afterId != null && afterId > 0) {
            return chatMessageRepository
                    .findByConsultation_ConsultationIdAndMessageIdGreaterThanOrderBySentAtAsc(consultationId, afterId);
        }
        return chatMessageRepository.findByConsultation_ConsultationIdOrderBySentAtAsc(consultationId);
    }
}
