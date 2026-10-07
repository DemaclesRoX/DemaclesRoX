package com.khamarbd.backend.service.support;

import com.khamarbd.backend.dto.SystemTemplateRequestDto;
import com.khamarbd.backend.entity.support.SystemTemplate;
import com.khamarbd.backend.repository.support.SystemTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemTemplateService {

    @Autowired
    private SystemTemplateRepository systemTemplateRepository;

    public SystemTemplate saveTemplate(SystemTemplateRequestDto dto) {
        SystemTemplate template = new SystemTemplate();
        String code = "TPL-" + dto.getSector().substring(0, Math.min(3, dto.getSector().length())).toUpperCase() + "-" + System.currentTimeMillis();
        template.setTemplateCode(code);
        template.setTemplateType(dto.getTemplateType());
        template.setSector(dto.getSector());
        template.setNameEn(dto.getTemplateName());
        template.setNameBn(dto.getTemplateName());
        template.setContentJson(dto.getContentJson());
        template.setOwnerUserId(dto.getOwnerUserId());
        return systemTemplateRepository.save(template);
    }

    public List<SystemTemplate> getAllTemplates() {
        return systemTemplateRepository.findAll();
    }

    public SystemTemplate getTemplateByCode(String code) {
        return systemTemplateRepository.findById(code).orElse(null);
    }

    public List<SystemTemplate> getTemplatesByTypeAndUserId(String type, Long userId) {
        return systemTemplateRepository.findByTypeAndUserIdForFarmer(type, userId);
    }

    public List<SystemTemplate> getTemplatesByType(String type) { return systemTemplateRepository.findByTemplateType(type); }

    public List<SystemTemplate> getTemplatesByTypeAndSector(String type, String sector) {
        return systemTemplateRepository.findByTemplateTypeAndSector(type, sector);
    }
}




