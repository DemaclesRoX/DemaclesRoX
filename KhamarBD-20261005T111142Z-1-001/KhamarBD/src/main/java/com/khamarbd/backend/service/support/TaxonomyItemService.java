package com.khamarbd.backend.service.support;

import com.khamarbd.backend.dto.TaxonomyItemRequestDto;
import com.khamarbd.backend.entity.support.TaxonomyItem;
import com.khamarbd.backend.repository.support.TaxonomyItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxonomyItemService {

    @Autowired
    private TaxonomyItemRepository taxonomyItemRepository;

    public TaxonomyItem saveTaxonomyItem(TaxonomyItemRequestDto dto) {
        TaxonomyItem item = new TaxonomyItem();
        String code = "TAX-" + dto.getDomain().toUpperCase() + "-" + System.currentTimeMillis();
        item.setItemCode(code);
        item.setDomain(dto.getDomain());
        item.setSector(dto.getSector());
        item.setCategoryCode(dto.getCategoryCode());
        item.setNameBn(dto.getNameBn());
        item.setNameEn(dto.getNameEn());
        item.setIsActive(true);
        return taxonomyItemRepository.save(item);
    }

    public List<TaxonomyItem> getAllTaxonomyItems() {
        return taxonomyItemRepository.findAll();
    }

    public List<TaxonomyItem> getItemsByDomain(String domain) {
        return taxonomyItemRepository.findByDomainAndIsActiveTrue(domain);
    }

    public List<TaxonomyItem> getItemsByDomainAndSector(String domain, String sector) {
        return taxonomyItemRepository.findByDomainAndSectorAndIsActiveTrue(domain, sector);
    }
}
