package com.github.pmbdev.global_finance_api.repository.entity;

import com.github.pmbdev.global_finance_api.repository.entity.enums.TransactionCategory;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity // To make the code to database
@Table(name = "transactions") // Name of the table in the database
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_account_id", nullable = false)
    private AccountEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_account_id", nullable = false)
    private AccountEntity receiver;

    @Column
    private String concept;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionCategory category;

}
