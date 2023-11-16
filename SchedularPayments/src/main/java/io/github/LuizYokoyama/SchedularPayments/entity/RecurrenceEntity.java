package io.github.LuizYokoyama.SchedularPayments.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    @OneToOne(fetch = FetchType.LAZY)
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

}
