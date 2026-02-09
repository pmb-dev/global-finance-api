package com.github.pmbdev.global_finance_api.repository;

import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t WHERE t.sender.user.id = :userId OR t.receiver.user.id = :userId ORDER BY t.timestamp DESC")
    List<TransactionEntity> findAllByUserId(@Param("userId") Long userId);
}