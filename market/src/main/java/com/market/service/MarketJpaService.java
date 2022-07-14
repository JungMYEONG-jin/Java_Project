package com.market.service;

import com.market.entity.Market;
import com.market.repository.MarketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
public class MarketJpaService {

    private final MarketRepository marketRepository;

    public MarketJpaService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    public void save(Market market){
        marketRepository.save(market);
    }

    @Transactional
    public void delete(Market market){
        marketRepository.deleteById(market.getId());
    }


}
