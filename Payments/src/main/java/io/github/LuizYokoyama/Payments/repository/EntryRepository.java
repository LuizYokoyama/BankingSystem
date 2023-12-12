package io.github.LuizYokoyama.Payments.repository;


import io.github.LuizYokoyama.Payments.dto.EntryDto;
import io.github.LuizYokoyama.Payments.entity.AccountEntity;
import io.github.LuizYokoyama.Payments.entity.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository("entryRepository")
public interface EntryRepository extends JpaRepository<EntryEntity, UUID> {

    @Query("SELECT new io.github.LuizYokoyama.Payments.dto.EntryDto(e.entryId, :id, e.operationType, e.value, e.entryDateTime, e.entryStatus) " +
            "FROM EntryEntity e WHERE e.accountEntity.accountId = :id AND e.entryDateTime >= :initDate AND e.entryDateTime <= :endDate " +
            " ORDER BY e.entryDateTime")
    List<EntryDto> getStatement(@Param("id") Integer id, @Param("initDate") LocalDateTime initDate, @Param("endDate") LocalDateTime endDate);

    @Query("""
            SELECT SUM(e.value) 
            FROM EntryEntity e JOIN e.accountEntity a 
            WHERE e.entryStatus = io.github.LuizYokoyama.Payments.entity.EntryStatus.DONE 
            AND e.entryDateTime >= a.aggregationDateTime AND a.accountId = :accountId
            """ )
    BigDecimal aggregateBalanceSince(@Param("accountId") int accountId);

}
