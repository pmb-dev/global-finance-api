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

    @Email(message = "You must introduce a valid email.")
    @NotBlank(message = "The email can't be blank.")
    @Column(unique = true, nullable = false) //email with restriction & not null
    private String email;

    @NotBlank(message = "The password can't be null.")
    @Size(min = 6, message = "The password must have at least six characters.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //So the password won't be sent to the JSON and to the front end
    private String password;
}
