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

        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(createRecurrenceDto.getAccountId());
        if (!accountEntityOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (createRecurrenceDto.getOccurrenceDate().getMonth() == LocalDate.now().getMonth()){

            createRecurrenceDto.setDuration(createRecurrenceDto.getDuration() - 1);

            EntryEntity entryEntity = new EntryEntity();
            entryEntity.setAccountEntity(accountEntityOptional.get());
            entryEntity.setEntryDateTime(LocalDateTime.now());
            entryEntity.setValue(createRecurrenceDto.getValue());
            entryEntity.setOperationType(OperationType.DEBIT);
            entryEntity.setEntryStatus(EntryStatus.PENDING);
            entryEntity = entryRepository.save(entryEntity);

            createRecurrenceDto.getAccountDestinationID();

        }


        RecurrenceEntity recurrenceEntity = new RecurrenceEntity();
        BeanUtils.copyProperties(createRecurrenceDto, recurrenceEntity);
        recurrenceEntity = recurrenceRepository.save(recurrenceEntity);



        RecurrenceDto recurrenceDto = new RecurrenceDto();
        BeanUtils.copyProperties(recurrenceEntity, recurrenceDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(recurrenceDto);
    }
}
