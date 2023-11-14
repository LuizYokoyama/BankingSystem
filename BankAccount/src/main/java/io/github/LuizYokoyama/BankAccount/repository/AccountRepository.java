package io.github.LuizYokoyama.BankAccount.repository;

import io.github.LuizYokoyama.BankAccount.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
}
