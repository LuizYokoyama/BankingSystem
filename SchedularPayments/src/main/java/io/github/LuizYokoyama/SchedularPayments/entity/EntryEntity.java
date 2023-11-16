package io.github.LuizYokoyama.SchedularPayments.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "origin_id")
    private AccountEntity originEntity;

    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @Column(name = "value", nullable = false)
    private float value;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDateTime;

    @Column(name = "status", nullable = false)
    private EntryStatus entryStatus;

}
