package io.github.LuizYokoyama.BankAccount.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="TB_RECURRENCE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecurrenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "recurrence_id")
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "account_id")
    private AccountEntity accountEntity;

    @Column(name = "recurrence_name", nullable = false)
    private String recurrenceName;

    @Column(name = "recurrence_date", nullable = false)
    private LocalDate occurrenceDate;

    @Column(name = "recurrence_duration", nullable = false)
    private int duration;

    @Column(name = "value", nullable = false)
    private float value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "account_destination_id")
    private AccountEntity accountDestination;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "recurrence_id")
    private Set<EntryEntity> entrySet;

    @Column(name = "status", nullable = false)
    private RecurrenceStatus recurrenceStatus;

}
