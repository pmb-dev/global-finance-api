package com.github.pmbdev.global_finance_api.repository;

import com.github.pmbdev.global_finance_api.repository.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Gets all the methods from Spring Data: save, select, insert...
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    Optional<UserEntity> findByEmail(String email);
}
