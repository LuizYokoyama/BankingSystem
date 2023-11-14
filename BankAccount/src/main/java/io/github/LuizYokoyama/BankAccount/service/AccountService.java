package io.github.LuizYokoyama.BankAccount.service;

import io.github.LuizYokoyama.BankAccount.dto.AccountCreatedDto;
import io.github.LuizYokoyama.BankAccount.dto.AccountDto;
import io.github.LuizYokoyama.BankAccount.dto.CreateAccountDto;
import io.github.LuizYokoyama.BankAccount.dto.DepositDto;
import io.github.LuizYokoyama.BankAccount.entity.AccountEntity;
import io.github.LuizYokoyama.BankAccount.repository.AccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<AccountCreatedDto> createAccount(CreateAccountDto accountDto){

        AccountEntity accountEntity = new AccountEntity();
        BeanUtils.copyProperties(accountDto, accountEntity);

        accountEntity = accountRepository.save(accountEntity);

        AccountCreatedDto accountCreatedDto = new AccountCreatedDto();
        BeanUtils.copyProperties(accountEntity, accountCreatedDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreatedDto);
    }

    public ResponseEntity<AccountCreatedDto> deposit(Integer id, DepositDto depositDto) {

        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(id);
        if (!accountEntityOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        AccountEntity accountEntity = accountEntityOptional.get();
        accountEntity.setBalance(accountEntity.getBalance() + depositDto.getValue());
        accountEntity = accountRepository.save(accountEntity);

        AccountCreatedDto accountCreatedDto = new AccountCreatedDto();
        BeanUtils.copyProperties(accountEntity, accountCreatedDto);
        return ResponseEntity.status(HttpStatus.OK).body(accountCreatedDto);

    }
}
