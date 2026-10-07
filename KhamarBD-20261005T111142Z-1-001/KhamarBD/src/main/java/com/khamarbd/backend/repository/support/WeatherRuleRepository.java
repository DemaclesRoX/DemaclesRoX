package com.khamarbd.backend.repository.support;

import com.khamarbd.backend.entity.support.WeatherRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRuleRepository extends JpaRepository<WeatherRule, Long> {
    List<WeatherRule> findBySector(String sector);
}
