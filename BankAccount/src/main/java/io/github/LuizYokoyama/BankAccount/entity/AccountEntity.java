package io.github.LuizYokoyama.BankAccount.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name="TB_ACCOUNT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountEntity {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private int accountId;

    @Column(name = "holder_name", nullable = false)
    private String holderName;

    @Column(name = "balance", nullable = false)
    private float balance;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Set<RecurrenceEntity> recurrenceList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Set<EntryEntity> entryList;
}
