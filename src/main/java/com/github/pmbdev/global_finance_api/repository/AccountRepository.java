package com.github.pmbdev.global_finance_api.repository;

import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findByUser(UserEntity user);
    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    @Query("SELECT a.currency, SUM(a.balance) FROM AccountEntity a GROUP BY a.currency")
    List<Object[]> getTotalBalancesByCurrency();

    @Query("SELECT a.currency, SUM(a.balance) FROM AccountEntity a GROUP BY a.currency")
    List<Object[]> findTotalBalancesByCurrency();
}
