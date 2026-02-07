package com.github.pmbdev.global_finance_api.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity // To make the code to database
@Table(name = "accounts") // Name of the table in the database
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    // Relation Many To One -> One user can have several accounts
    @ManyToOne(fetch = FetchType.LAZY) // Lazy to not get the user's data if's not asked
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
