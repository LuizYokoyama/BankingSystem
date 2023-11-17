package io.github.LuizYokoyama.Payments.repository;

import io.github.LuizYokoyama.Payments.entity.RecurrenceEntity;
import io.github.LuizYokoyama.Payments.entity.RecurrenceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository("recurrenceRepository")
public interface RecurrenceRepository extends JpaRepository<RecurrenceEntity, UUID> {

    @Query("SELECT RecurrenceEntity " +
            "FROM RecurrenceEntity e WHERE e.recurrenceStatus = :pending")
    Set<RecurrenceEntity> getPendingRecurrences( @Param("pendig") RecurrenceStatus pending);

}
