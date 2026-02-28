package com.github.pmbdev.global_finance_api.repository;

import com.github.pmbdev.global_finance_api.controller.dto.CategoryStatResponse;
import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t WHERE (t.sender.user.id = :userId OR t.receiver.user.id = :userId) " +
            "AND (CAST(:startDate AS timestamp) IS NULL OR t.timestamp >= :startDate) " +
            "AND (CAST(:endDate AS timestamp) IS NULL OR t.timestamp <= :endDate)")
    Page<TransactionEntity> findAllByUserIdWithFilters(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    @Query("SELECT t.category as category, " +
            "SUM(t.amount) as totalAmount " +
            "FROM TransactionEntity t " +
            "WHERE t.sender.user.id = :userId " +
            "GROUP BY t.category")
    List<CategoryStatResponse> findSpendingByCategory(@Param("userId") Long userId);

    List<TransactionEntity> findBySenderOrReceiver(AccountEntity sender, AccountEntity receiver);
}