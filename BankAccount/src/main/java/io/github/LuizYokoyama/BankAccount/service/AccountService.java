package io.github.LuizYokoyama.BankAccount.service;

import io.github.LuizYokoyama.BankAccount.dto.*;
import io.github.LuizYokoyama.BankAccount.entity.AccountEntity;
import io.github.LuizYokoyama.BankAccount.entity.EntryEntity;
import io.github.LuizYokoyama.BankAccount.entity.EntryStatus;
import io.github.LuizYokoyama.BankAccount.entity.OperationType;
import io.github.LuizYokoyama.BankAccount.exceptions.DataBaseException;
import io.github.LuizYokoyama.BankAccount.exceptions.NotFoundRuntimeException;
import io.github.LuizYokoyama.BankAccount.exceptions.ValueZeroRuntimeException;
import io.github.LuizYokoyama.BankAccount.repository.AccountRepository;
import io.github.LuizYokoyama.BankAccount.repository.EntryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    public AccountCreatedDto createAccount(CreateAccountDto accountDto){

        AccountEntity accountEntity = new AccountEntity();
        BeanUtils.copyProperties(accountDto, accountEntity);

        try {
            accountEntity = accountRepository.save(accountEntity);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao salvar a conta!", ex.getCause());
        }

        AccountCreatedDto accountCreatedDto = new AccountCreatedDto();
        BeanUtils.copyProperties(accountEntity, accountCreatedDto);

        return accountCreatedDto;
    }

    @Transactional
    public EntryDto deposit(Integer id, DepositDto depositDto) {

        if (depositDto.getValue() < MIN_DEPOSIT_VALUE){
            throw new ValueZeroRuntimeException("Forneça um valor maior que zero!");
        }

        AccountEntity accountEntity = findAccount(id);
        accountEntity.setBalance(accountEntity.getBalance() + depositDto.getValue());
        try {
            accountEntity = accountRepository.save(accountEntity);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao salvar a conta!", ex.getCause());
        }

        EntryEntity entryEntity = new EntryEntity();
        entryEntity.setAccountEntity(accountEntity);
        entryEntity.setEntryDateTime(LocalDateTime.now());
        entryEntity.setValue(depositDto.getValue());
        entryEntity.setOperationType(OperationType.CREDIT);
        entryEntity.setEntryStatus(EntryStatus.DONE);
        try {
            entryEntity = entryRepository.save(entryEntity);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao salvar a conta!", ex.getCause());
        }

        EntryDto entryDto = new EntryDto();
        BeanUtils.copyProperties(entryEntity, entryDto);
        entryDto.setAccountId(entryEntity.getAccountEntity().getAccountId());

        return entryDto;
    }

    public BankStatementDto statement(Integer id, PeriodDto periodDto) {

        AccountEntity accountEntity = findAccount(id);
        AccountCreatedDto accountCreatedDto = new AccountCreatedDto();
        BeanUtils.copyProperties(accountEntity, accountCreatedDto);
        List<EntryDto> entryList;
        try {
           entryList = entryRepository.getStatement(id,
                    periodDto.getInitDate().atTime(0, 0, 0),
                    periodDto.getEndDate().atTime(23, 59, 59));
        }catch (Exception ex){
            throw new DataBaseException("Falha ao buscar entradas da conta!", ex.getCause());
        }
        BankStatementDto bankStatementDto = new BankStatementDto(accountCreatedDto, periodDto, entryList);

        return  bankStatementDto;

    }

    private AccountEntity findAccount(Integer id){

        Optional<AccountEntity> accountEntityOptional;
        try {
            accountEntityOptional = accountRepository.findById(id);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao buscar a conta!", ex.getCause());
        }
        if (accountEntityOptional.isEmpty()){
            throw new NotFoundRuntimeException("Conta não encontrada. Forceça uma conta válida.");
        }
        return accountEntityOptional.get();

    }
}
