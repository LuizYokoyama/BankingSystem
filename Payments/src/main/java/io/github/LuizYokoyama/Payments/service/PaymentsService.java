package io.github.LuizYokoyama.Payments.service;

import io.github.LuizYokoyama.Payments.entity.*;
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
import java.util.Set;

@Service
@Validated
public class PaymentsService {

    @Autowired
    private RecurrenceRepository recurrenceRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntryRepository entryRepository;


    public int executePays(){
        int operationsCount = 0;
        Set<RecurrenceEntity> recurrenceEntitySet = recurrenceRepository.getPendingRecurrences(RecurrenceStatus.PENDING);

        LocalDateTime today = LocalDate.now().atTime(0, 0);

        for (RecurrenceEntity recurrence: recurrenceEntitySet){

            if (recurrence.getRecurrenceStatus() == RecurrenceStatus.CANCELED){
                continue;
            }
            if (recurrence.getRecurrenceStatus() == RecurrenceStatus.PARTLY_CANCELED){
                continue;
            }
            if (recurrence.getRecurrenceStatus() == RecurrenceStatus.DONE){
                continue;
            }

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

                        if (entryEntityIterator.hasNext()){
                            recurrence.setRecurrenceStatus(RecurrenceStatus.PARTLY_DONE);
                        } else{
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
        AccountEntity accountCredit = entryCredit.getAccountEntity();

        if (accountDebit.getBalance() < entryDebit.getValue()){
            return false;
        }

        entryDebit.setEntryStatus(EntryStatus.DONE);
        entryCredit.setEntryStatus(EntryStatus.DONE);
        accountDebit.setBalance(accountDebit.getBalance() - entryDebit.getValue());
        accountCredit.setBalance(accountCredit.getBalance() + entryDebit.getValue());

        recurrenceRepository.save(recurrence);

        return true;

    }


}
