package com.market.repository;

import com.market.entity.Send;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SendRepository extends JpaRepository<Send, Long> {
    @Query("select s from Send s where s.id = :id")
    Send getById(@Param("id") Long id);
}
