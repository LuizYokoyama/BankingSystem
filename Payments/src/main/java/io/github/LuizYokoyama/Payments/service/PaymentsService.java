package io.github.LuizYokoyama.Payments.service;

import io.github.LuizYokoyama.Payments.dto.RecurrenceDto;
import io.github.LuizYokoyama.Payments.entity.EntryEntity;
import io.github.LuizYokoyama.Payments.entity.RecurrenceEntity;
import io.github.LuizYokoyama.Payments.entity.RecurrenceStatus;
import io.github.LuizYokoyama.Payments.repository.AccountRepository;
import io.github.LuizYokoyama.Payments.repository.EntryRepository;
import io.github.LuizYokoyama.Payments.repository.RecurrenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

        Set<RecurrenceEntity> recurrenceEntitySet = recurrenceRepository.getPendingRecurrences(RecurrenceStatus.PENDING);

        for (RecurrenceEntity recurrence: recurrenceEntitySet){

            Set<EntryEntity> entryEntitySet = recurrence.getEntrySet();

        }


        return -1;
    }


}
