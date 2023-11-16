package io.github.LuizYokoyama.BankAccount.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="TB_ENTRY")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "entry_id")
    @EqualsAndHashCode.Include
    private UUID entryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "account_id")
    private AccountEntity accountEntity;

    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @Column(name = "value", nullable = false)
    private float value;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "status", nullable = false)
    private EntryStatus entryStatus;

}
