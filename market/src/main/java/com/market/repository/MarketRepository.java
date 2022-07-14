package com.market.repository;

import com.market.daemon.dto.SendInfo;
import com.market.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {

    @Query("select new com.market.daemon.dto.SendInfo(m, s) from Market m, Send s where m.appId = s.appId order by m.id")
    List<SendInfo> GET_SEND_INFO_LIST();


}
