package io.github.LuizYokoyama.SchedularPayments.repository;

import io.github.LuizYokoyama.SchedularPayments.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
}
