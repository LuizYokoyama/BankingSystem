package io.github.LuizYokoyama.BankAccount.service;

import io.github.LuizYokoyama.BankAccount.dto.*;
import io.github.LuizYokoyama.BankAccount.entity.AccountEntity;
import io.github.LuizYokoyama.BankAccount.entity.EntryEntity;
import io.github.LuizYokoyama.BankAccount.entity.EntryStatus;
import io.github.LuizYokoyama.BankAccount.entity.OperationType;
import io.github.LuizYokoyama.BankAccount.repository.AccountRepository;
import io.github.LuizYokoyama.BankAccount.repository.EntryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class AccountService {

    private static final float MIN_DEPOSIT_VALUE = 0.01f;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntryRepository entryRepository;

    public ResponseEntity<AccountCreatedDto> createAccount(CreateAccountDto accountDto){

        AccountEntity accountEntity = new AccountEntity();
        BeanUtils.copyProperties(accountDto, accountEntity);

        accountEntity = accountRepository.save(accountEntity);

        AccountCreatedDto accountCreatedDto = new AccountCreatedDto();
        BeanUtils.copyProperties(accountEntity, accountCreatedDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreatedDto);
    }

    @Transactional
    public ResponseEntity<EntryDto> deposit(Integer id, DepositDto depositDto) {

        if (depositDto.getValue() < MIN_DEPOSIT_VALUE){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(id);
        if (!accountEntityOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        AccountEntity accountEntity = accountEntityOptional.get();
        accountEntity.setBalance(accountEntity.getBalance() + depositDto.getValue());
        accountEntity = accountRepository.save(accountEntity);

        EntryEntity entryEntity = new EntryEntity();
        entryEntity.setAccountEntity(accountEntity);
        entryEntity.setEntryDateTime(LocalDateTime.now());
        entryEntity.setValue(depositDto.getValue());
        entryEntity.setOperationType(OperationType.CREDIT);
        entryEntity.setEntryStatus(EntryStatus.DONE);
        entryEntity = entryRepository.save(entryEntity);

        EntryDto entryDto = new EntryDto();
        BeanUtils.copyProperties(entryEntity, entryDto);
        entryDto.setAccountId(entryEntity.getAccountEntity().getAccountId());

        return ResponseEntity.status(HttpStatus.OK).body(entryDto);

    }

    public ResponseEntity<BankStatementDto> statement(Integer id, PeriodDto periodDto) {

        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(id);
        if ( !accountEntityOptional.isPresent() ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        AccountCreatedDto accountCreatedDto = new AccountCreatedDto();
        BeanUtils.copyProperties(accountEntityOptional.get(), accountCreatedDto);
        List<EntryDto> entryList = entryRepository.getStatement(id,
                periodDto.getInitDate().atTime(0, 0, 0),
                periodDto.getEndDate().atTime(0, 0, 0));
        BankStatementDto bankStatementDto = new BankStatementDto(accountCreatedDto, periodDto, entryList);
        return  ResponseEntity.status(HttpStatus.OK).body(bankStatementDto);
    }
}
