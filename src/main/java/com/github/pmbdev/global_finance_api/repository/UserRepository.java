package com.github.pmbdev.global_finance_api.repository;

import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;

import com.github.pmbdev.global_finance_api.repository.entity.enums.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//Gets all the methods from Spring Data: save, select, insert...
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByRole(UserRole role);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.enabled = :status WHERE u.id = :userId")
    void updateEnabledStatus(Long userId, boolean status);
}
