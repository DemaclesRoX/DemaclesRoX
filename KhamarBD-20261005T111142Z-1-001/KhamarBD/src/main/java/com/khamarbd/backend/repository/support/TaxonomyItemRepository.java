package com.khamarbd.backend.repository.support;

import com.khamarbd.backend.entity.support.TaxonomyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxonomyItemRepository extends JpaRepository<TaxonomyItem, String> {

    List<TaxonomyItem> findByDomainAndIsActiveTrue(String domain);

    List<TaxonomyItem> findByDomainAndSectorAndIsActiveTrue(String domain, String sector);

    List<TaxonomyItem> findByCategoryCodeAndIsActiveTrue(String categoryCode);

    List<TaxonomyItem> findByParentCodeAndIsActiveTrue(String parentCode);
}
