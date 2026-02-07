package com.github.pmbdev.global_finance_api.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//So the password won't be send to the JSON and to the front end
    private String password;
}
