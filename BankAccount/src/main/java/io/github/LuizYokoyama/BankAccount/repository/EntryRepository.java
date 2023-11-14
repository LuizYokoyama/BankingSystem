package io.github.LuizYokoyama.BankAccount.repository;

import io.github.LuizYokoyama.BankAccount.entity.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("entryRepository")
public interface EntryRepository extends JpaRepository<EntryEntity, UUID> {
}
