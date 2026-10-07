package com.khamarbd.backend.controller;

import com.khamarbd.backend.dto.WeatherRuleRequestDto;
import com.khamarbd.backend.entity.support.WeatherRule;
import com.khamarbd.backend.repository.support.WeatherRuleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather-rule")
public class WeatherRuleController {

    @Autowired
    private WeatherRuleRepository weatherRuleRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createWeatherRule(
            @Valid @RequestBody WeatherRuleRequestDto weatherRuleRequestDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        WeatherRule rule = new WeatherRule();
        rule.setSector(weatherRuleRequestDto.getSector());
        rule.setConditionParameter(weatherRuleRequestDto.getConditionParameter());
        rule.setOperator(weatherRuleRequestDto.getOperator());
        rule.setThresholdValue(weatherRuleRequestDto.getThresholdValue());
        rule.setAlertMessage(weatherRuleRequestDto.getAlertMessage());

        WeatherRule savedRule = weatherRuleRepository.save(rule);

        return ResponseEntity.ok(savedRule);
    }

    @GetMapping("/active")
    public ResponseEntity<List<WeatherRule>> getActiveRules(@RequestParam String sector) {
        List<WeatherRule> rules = weatherRuleRepository.findBySector(sector);
        return ResponseEntity.ok(rules);
    }
}
