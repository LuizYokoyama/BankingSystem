package io.github.LuizYokoyama.BankAccount.repository;

import io.github.LuizYokoyama.BankAccount.dto.EntryDto;
import io.github.LuizYokoyama.BankAccount.entity.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository("entryRepository")
public interface EntryRepository extends JpaRepository<EntryEntity, UUID> {

    @Query("SELECT new io.github.LuizYokoyama.BankAccount.dto.EntryDto(e.entryId, :id, e.operationType, e.value, e.entryDate, e.entryStatus)" +
            "FROM EntryEntity e WHERE e.accountEntity.accountId = :id AND e.entryDate >= :initDate AND e.entryDate <= :endDate")
    List<EntryDto> getStatement(@Param("id") Integer id, @Param("initDate") LocalDate initDate, @Param("endDate") LocalDate endDate);

}
