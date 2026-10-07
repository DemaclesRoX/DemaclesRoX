package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.ChatMessageRequestDto;
import com.khamarbd.backend.entity.ChatMessage;
import com.khamarbd.backend.service.ChatMessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    // @RequestBody - farmer or specialist sends a message inside an accepted consultation
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @Valid @RequestBody ChatMessageRequestDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            ChatMessage saved = chatMessageService.sendMessage(dto);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // @PathVariable + @RequestParam - load thread; afterId lets the UI poll only new messages
    @GetMapping("/consultation/{consultationId}")
    public ResponseEntity<List<ChatMessage>> getMessages(
            @PathVariable Long consultationId,
            @RequestParam(required = false) Long afterId
    ) {
        return ResponseEntity.ok(chatMessageService.getMessages(consultationId, afterId));
    }
}
