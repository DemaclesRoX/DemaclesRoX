package com.khamarbd.backend.repository;

import com.khamarbd.backend.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByConsultation_ConsultationIdOrderBySentAtAsc(Long consultationId);

    List<ChatMessage> findByConsultation_ConsultationIdAndMessageIdGreaterThanOrderBySentAtAsc(Long consultationId, Long afterId);
}
