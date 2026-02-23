package com.github.pmbdev.global_finance_api.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.pmbdev.global_finance_api.repository.entity.enums.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity // To make the code to database
@Table(name = "accounts") // Name of the table in the database
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING) // enum as string in DB
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal balance;

    // Relation Many To One -> One user can have several accounts
    @ManyToOne(fetch = FetchType.LAZY) // Lazy to not get the user's data if's not asked
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
