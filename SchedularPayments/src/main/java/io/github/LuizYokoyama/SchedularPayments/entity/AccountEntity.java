package io.github.LuizYokoyama.SchedularPayments.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
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
    @EqualsAndHashCode.Include
    private int accountId;

    @Column(name = "holder_name", nullable = false)
    private String holderName;

    @Column(name = "balance", nullable = false)
    private float balance;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    @Valid
    private Set<RecurrenceEntity> recurrenceList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    @Valid
    private Set<EntryEntity> entryList;
}
