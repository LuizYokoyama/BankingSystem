package io.github.LuizYokoyama.SchedularPayments.repository;

import io.github.LuizYokoyama.SchedularPayments.entity.RecurrenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("recurrenceRepository")
public interface RecurrenceRepository extends JpaRepository<RecurrenceEntity, UUID> {
}
