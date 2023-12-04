package io.github.LuizYokoyama.SchedularPayments.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(name = "aggregated_balance", nullable = false)
    private float balance;

    @Column(name = "aggregation_date_time", nullable = false)
    private LocalDateTime entryDateTime;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Set<RecurrenceEntity> recurrenceList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Set<EntryEntity> entryList;
}
