package com.github.pmbdev.global_finance_api.repository.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // To make the code to database
@Table(name = "users") // Name of the table in the database
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserEntity {

    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id will be 1,2,3...
    private Long id;

    private String name;

    @Column(unique = true, nullable = false) //email with restriction & not null
    private String email;

    @Column(nullable = false)
    private String password;
}
