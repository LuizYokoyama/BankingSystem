package io.github.LuizYokoyama.BankAccount.repository;

import io.github.LuizYokoyama.BankAccount.entity.RecurrenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("recurrenceRepository")
public interface RecurrenceRepository extends JpaRepository<RecurrenceEntity, UUID> {
}
