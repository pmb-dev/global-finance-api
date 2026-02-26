package com.github.pmbdev.global_finance_api.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

@Entity // To make the code to database
@Table(name = "users") // Name of the table in the database
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity {

    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id will be 1,2,3...
    private Long id;

    @NotBlank(message = "The name can't be null.")
    private String name;

    @Email(message = "You must introduce a valid email format.")
    @NotBlank(message = "The email cannot be empty.")
    @Column(unique = true, nullable = false) //email with restriction & not null
    private String email;

    @NotBlank(message = "The password cannot be empty.")
    @Size(min = 8, message = "The password must be at least 8 characters long.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //So the password won't be sent to the JSON and to the front end
    private String password;
}
