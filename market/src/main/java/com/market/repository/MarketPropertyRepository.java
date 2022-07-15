package com.market.repository;

import com.market.entity.MarketPropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketPropertyRepository extends JpaRepository<MarketPropertyEntity, Long> {

    MarketPropertyEntity findFirstByOrderByRegDt();
}
