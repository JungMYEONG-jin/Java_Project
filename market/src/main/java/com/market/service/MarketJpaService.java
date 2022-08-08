package com.market.service;

import com.market.entity.Market;
import com.market.repository.MarketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarketJpaService {

    private final MarketRepository marketRepository;

    public MarketJpaService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    public void save(Market market){
        marketRepository.save(market);
    }

    /**
     * spring boot 2.xxx deleteById
     * spring boot 1.xxx delete
     * @param market
     */
    @Transactional
    public void delete(Market market){
//        marketRepository.deleteById(market.getId());
        marketRepository.delete(market.getId());
    }


}
