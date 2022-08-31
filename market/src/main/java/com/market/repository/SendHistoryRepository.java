package com.market.repository;

import com.market.entity.SendHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SendHistoryRepository extends JpaRepository<SendHistory, Long> {
}
