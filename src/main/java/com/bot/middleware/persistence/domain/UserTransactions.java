package com.bot.middleware.persistence.domain;
import com.bot.middleware.persistence.type.AwardType;
import com.bot.middleware.persistence.type.TransactionType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "user_transactions", schema = "public")
public class UserTransactions extends BaseEntity implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false, unique = true, nullable = false, name ="id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(updatable = false, unique = true, nullable = false, name ="public_id")
    private Long publicId;

    @Column(nullable = false, name = "transaction_type")
    private String transactionType;

    @Column(nullable = false, name ="amount")
    private BigDecimal amount;

    @Column(nullable = false, name ="purpose")
    private String purpose;

    @Column(nullable = false, name ="description")
    private String description;

    @Column(nullable = false, name ="category")
    private String category;

    @Column(nullable = false, name = "payment_id")
    private String paymentId;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name="user_id", referencedColumnName="public_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @Builder.Default
    @JoinColumn(name = "task_id", referencedColumnName = "public_id")
    private Task task;

    public UserTransactions() {}
}
