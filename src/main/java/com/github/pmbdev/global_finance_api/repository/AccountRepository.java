package com.github.pmbdev.global_finance_api.repository;

import com.github.pmbdev.global_finance_api.repository.entity.AccountEntity;
import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findByUser(UserEntity user);
}
