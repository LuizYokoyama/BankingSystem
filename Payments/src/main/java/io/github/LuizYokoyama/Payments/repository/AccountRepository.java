package io.github.LuizYokoyama.Payments.repository;

import io.github.LuizYokoyama.Payments.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
}
