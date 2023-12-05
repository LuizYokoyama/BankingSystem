package io.github.LuizYokoyama.Payments.service;

import io.github.LuizYokoyama.Payments.entity.*;
import io.github.LuizYokoyama.Payments.exception.DataBaseException;
import io.github.LuizYokoyama.Payments.repository.AccountRepository;
import io.github.LuizYokoyama.Payments.repository.EntryRepository;
import io.github.LuizYokoyama.Payments.repository.RecurrenceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@Validated
public class PaymentsService {

    @Autowired
    private RecurrenceRepository recurrenceRepository;
    @Autowired
    private EntryRepository entryRepository;
    @Autowired
    private AccountRepository accountRepository;


    public int executePays(){
        int operationsCount = 0;
        Set<RecurrenceEntity> recurrenceEntitySet;
        try {
            recurrenceEntitySet = recurrenceRepository.getPendingRecurrences();
        }catch (Exception ex){
            throw new DataBaseException("Falha ao buscar recorrências pendentes!", ex.getCause());
        }

        LocalDateTime today = LocalDate.now().atTime(0, 0);

        for (RecurrenceEntity recurrence: recurrenceEntitySet){

            EntryEntity entryDebit = null;
            EntryEntity entryCredit = null;

            //find the pair debit/credit entries
            Iterator<EntryEntity> entryEntityIterator = recurrence.getEntrySet().iterator();
            while (entryEntityIterator.hasNext()) {

                EntryEntity entry = entryEntityIterator.next();
                if (entry.getEntryStatus() == EntryStatus.PENDING){

                    if (entry.getEntryDateTime().isEqual(today)) {
                        if (entry.getOperationType() == OperationType.DEBIT){
                            entryDebit = entry;
                        } else{
                            entryCredit = entry;
                        }
                    }

                    if (entryCredit != null && entryDebit != null){
                        if ( !entryEntityIterator.hasNext() ){
                            recurrence.setRecurrenceStatus(RecurrenceStatus.DONE);
                        }

                        Boolean success = executePay(recurrence, entryCredit, entryDebit);
                        if ( success ){
                            operationsCount++;
                        }
                        break;
                    }
                }
            }
        }

        return operationsCount;
    }

    @Transactional
    private boolean executePay(RecurrenceEntity recurrence, EntryEntity entryCredit, EntryEntity entryDebit) {

        AccountEntity accountDebit = entryDebit.getAccountEntity();

        float balance = getBalance(accountDebit);

        if (balance < entryDebit.getValue()){
            return false;
        }

        entryDebit.setEntryStatus(EntryStatus.DONE);
        entryDebit.setRecurrenceEntity(null);
        entryCredit.setEntryStatus(EntryStatus.DONE);
        entryCredit.setRecurrenceEntity(null);
        entryDebit.setEntryDateTime(LocalDateTime.now());
        entryCredit.setEntryDateTime(LocalDateTime.now());

        try {
            recurrenceRepository.save(recurrence);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao salvar recorrências!", ex.getCause());
        }

        return true;
    }

    public int executeAggregation() {

        int operationsCount = 0;
        List<AccountEntity> accountEntityList;

        try {
            accountEntityList = accountRepository.findAll();
        }catch (Exception ex){
            throw new DataBaseException("Falha ao buscar contas!", ex.getCause());
        }

        for (AccountEntity account: accountEntityList){
            if (aggregateBalance(account)){
                operationsCount++;
            }
        }

        return operationsCount;
    }

    @Transactional
    private boolean aggregateBalance(AccountEntity account) {

        Float aggregated;

        aggregated = aggregate(account);
        if (aggregated == null){
            return false;
        }
        account.setAggregatedBalance(aggregated + account.getAggregatedBalance());
        account.setAggregationDateTime(LocalDateTime.now());

        try {
            accountRepository.save(account);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao salvar conta!", ex.getCause());
        }
        return true;

    }

    private float getBalance(AccountEntity account){

        float lastAggregatedBalance = account.getAggregatedBalance();
        LocalDateTime lastTime = account.getAggregationDateTime();
        Float newAggregatedBalance;
        try {
            newAggregatedBalance = entryRepository.aggregateBalanceSince(lastTime, account);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao buscar o saldo da conta!", ex.getCause());
        }

        if (newAggregatedBalance == null){
            return lastAggregatedBalance;
        }

        return lastAggregatedBalance + newAggregatedBalance;
    }

    private Float aggregate(AccountEntity account){

        LocalDateTime lastTime = account.getAggregationDateTime();
        Float aggregated;
        try {
            aggregated = entryRepository.aggregateBalanceSince(lastTime, account);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao buscar o saldo da conta!", ex.getCause());
        }

        return aggregated;
    }

}
