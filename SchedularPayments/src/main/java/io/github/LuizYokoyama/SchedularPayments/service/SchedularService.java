package io.github.LuizYokoyama.SchedularPayments.service;

import io.github.LuizYokoyama.SchedularPayments.dto.CreateRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.RecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.entity.*;
import io.github.LuizYokoyama.SchedularPayments.repository.AccountRepository;
import io.github.LuizYokoyama.SchedularPayments.repository.EntryRepository;
import io.github.LuizYokoyama.SchedularPayments.repository.RecurrenceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

@Service
@Validated
public class SchedularService {


    @Autowired
    private RecurrenceRepository recurrenceRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Transactional
    public ResponseEntity<RecurrenceDto> schedule(CreateRecurrenceDto createRecurrenceDto) {

        if (createRecurrenceDto.getValue() == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (createRecurrenceDto.getOccurrenceDate().isBefore(LocalDate.now())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(createRecurrenceDto.getAccountId());
        if (!accountEntityOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<AccountEntity> accountDestinationEntityOptional = accountRepository.findById(createRecurrenceDto.getAccountDestinationID());
        if (!accountDestinationEntityOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        for (int i = 0; i < createRecurrenceDto.getDuration(); i++){

            LocalDateTime entryDate = createRecurrenceDto.getOccurrenceDate().plusMonths(i).atTime(0, 0);

            // CREDIT
            EntryEntity entryEntityToCredit = new EntryEntity();
            entryEntityToCredit.setAccountEntity(accountDestinationEntityOptional.get()); //will receive the amount
            entryEntityToCredit.setOriginEntity(accountEntityOptional.get()); //will send the amount
            entryEntityToCredit.setEntryDateTime(entryDate);
            entryEntityToCredit.setValue(createRecurrenceDto.getValue());
            entryEntityToCredit.setOperationType(OperationType.CREDIT);
            entryEntityToCredit.setEntryStatus(EntryStatus.PENDING);
            entryEntityToCredit = entryRepository.save(entryEntityToCredit);

            // DEBIT
            EntryEntity entryEntityToDebit = new EntryEntity();
            entryEntityToDebit.setAccountEntity(accountEntityOptional.get()); //will send the amount
            entryEntityToDebit.setOriginEntity(accountDestinationEntityOptional.get()); //will receive the amount
            entryEntityToDebit.setEntryDateTime(entryDate);
            entryEntityToDebit.setValue(createRecurrenceDto.getValue());
            entryEntityToDebit.setOperationType(OperationType.DEBIT);
            entryEntityToDebit.setEntryStatus(EntryStatus.PENDING);
            entryEntityToDebit = entryRepository.save(entryEntityToDebit);

        }


        RecurrenceEntity recurrenceEntity = new RecurrenceEntity();
        BeanUtils.copyProperties(createRecurrenceDto, recurrenceEntity);
        recurrenceEntity = recurrenceRepository.save(recurrenceEntity);



        RecurrenceDto recurrenceDto = new RecurrenceDto();
        BeanUtils.copyProperties(recurrenceEntity, recurrenceDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(recurrenceDto);
    }
}
