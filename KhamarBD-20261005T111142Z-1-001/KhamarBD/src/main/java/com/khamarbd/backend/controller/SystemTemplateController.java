package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.SystemTemplateRequestDto;
import com.khamarbd.backend.entity.support.SystemTemplate;
import com.khamarbd.backend.service.support.SystemTemplateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/template")
public class SystemTemplateController {

    @Autowired
    private SystemTemplateService systemTemplateService;

    // @RequestBody — admin or specialist creates a routine/prescription template
    @PostMapping("/create")
    public ResponseEntity<?> createTemplate(
            @Valid @RequestBody SystemTemplateRequestDto systemTemplateRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }
        SystemTemplate saved = systemTemplateService.saveTemplate(systemTemplateRequestDto);
        return ResponseEntity.ok(saved);
    }

    // @RequestParam — search templates by type and sector
    @GetMapping("/search")
    public ResponseEntity<List<SystemTemplate>> searchTemplates(
            @RequestParam String templateType,
            @RequestParam(required = false) String sector
    ) {
        List<SystemTemplate> templates; if (sector != null && !sector.isEmpty()) { templates = systemTemplateService.getTemplatesByTypeAndSector(templateType, sector); } else { templates = systemTemplateService.getTemplatesByType(templateType); }
        return ResponseEntity.ok(templates);
    }

    // @PathVariable — fetch template by code
    @GetMapping("/{templateCode}")
    public ResponseEntity<?> getTemplateByCode(@PathVariable String templateCode) {
        SystemTemplate template = systemTemplateService.getTemplateByCode(templateCode);
        if (template == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(template);
    }
}


