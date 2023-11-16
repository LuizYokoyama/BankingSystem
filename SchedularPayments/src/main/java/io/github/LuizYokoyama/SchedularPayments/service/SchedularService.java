package io.github.LuizYokoyama.SchedularPayments.service;

import io.github.LuizYokoyama.SchedularPayments.dto.CreateRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.EditRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.EntryDto;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

        RecurrenceEntity recurrenceEntity = new RecurrenceEntity();
        BeanUtils.copyProperties(createRecurrenceDto, recurrenceEntity);
        recurrenceEntity.setAccountEntity(accountEntityOptional.get());
        recurrenceEntity.setAccountDestination(accountDestinationEntityOptional.get());
        recurrenceEntity.setRecurrenceStatus(RecurrenceStatus.PENDING);
        recurrenceEntity = recurrenceRepository.save(recurrenceEntity);

        Set<EntryEntity> entrySet = new HashSet<>();

        for (int i = 0; i < createRecurrenceDto.getDuration(); i++){

            LocalDateTime entryDate = createRecurrenceDto.getOccurrenceDate().plusMonths(i).atTime(0, 0);

            // CREDIT
            EntryEntity entryEntityToCredit = new EntryEntity();
            entryEntityToCredit.setRecurrenceEntity(recurrenceEntity);
            entryEntityToCredit.setAccountEntity(accountDestinationEntityOptional.get()); //will receive the amount
            entryEntityToCredit.setOriginEntity(accountEntityOptional.get()); //will send the amount
            entryEntityToCredit.setEntryDateTime(entryDate);
            entryEntityToCredit.setValue(createRecurrenceDto.getValue());
            entryEntityToCredit.setOperationType(OperationType.CREDIT);
            entryEntityToCredit.setEntryStatus(EntryStatus.PENDING);
            entryEntityToCredit = entryRepository.save(entryEntityToCredit);
            entrySet.add(entryEntityToCredit);

            // DEBIT
            EntryEntity entryEntityToDebit = new EntryEntity();
            entryEntityToDebit.setRecurrenceEntity(recurrenceEntity);
            entryEntityToDebit.setAccountEntity(accountEntityOptional.get()); //will send the amount
            entryEntityToDebit.setOriginEntity(accountDestinationEntityOptional.get()); //will receive the amount
            entryEntityToDebit.setEntryDateTime(entryDate);
            entryEntityToDebit.setValue(createRecurrenceDto.getValue());
            entryEntityToDebit.setOperationType(OperationType.DEBIT);
            entryEntityToDebit.setEntryStatus(EntryStatus.PENDING);
            entryEntityToDebit = entryRepository.save(entryEntityToDebit);
            entrySet.add(entryEntityToDebit);

        }
        recurrenceEntity.setEntrySet(entrySet);
        recurrenceEntity = recurrenceRepository.save(recurrenceEntity);

        RecurrenceDto recurrenceDto = new RecurrenceDto();
        BeanUtils.copyProperties(recurrenceEntity, recurrenceDto);
        recurrenceDto.setAccountId(createRecurrenceDto.getAccountId());
        recurrenceDto.setAccountDestinationID(createRecurrenceDto.getAccountDestinationID());

        return ResponseEntity.status(HttpStatus.CREATED).body(recurrenceDto);
    }

    public ResponseEntity<RecurrenceDto> editScheduled(UUID uuid, EditRecurrenceDto editRecurrenceDto) {

        Optional<RecurrenceEntity> recurrenceEntityOptional = recurrenceRepository.findById(uuid);
        if (!recurrenceEntityOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        RecurrenceEntity recurrenceEntity = recurrenceEntityOptional.get();

        if (recurrenceEntity.getRecurrenceStatus() == RecurrenceStatus.CANCELED ||
                recurrenceEntity.getRecurrenceStatus() == RecurrenceStatus.DONE ||
                recurrenceEntity.getRecurrenceStatus() == RecurrenceStatus.PARTLY_CANCELED){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (recurrenceEntity.getValue() == editRecurrenceDto.getValue() &&
                recurrenceEntity.getDuration() == editRecurrenceDto.getDuration() &
                recurrenceEntity.getOccurrenceDate() == editRecurrenceDto.getOccurrenceDate()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (editRecurrenceDto.getValue() == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (editRecurrenceDto.getOccurrenceDate().isBefore(LocalDate.now())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        AccountEntity accountEntity = recurrenceEntity.getAccountEntity();

        AccountEntity accountDestinationEntity = recurrenceEntity.getAccountDestination();



        recurrenceEntity.setRecurrenceStatus(RecurrenceStatus.PENDING);
        recurrenceEntity = recurrenceRepository.save(recurrenceEntity);


        recurrenceEntity = recurrenceRepository.save(recurrenceEntity);

        RecurrenceDto recurrenceDto = new RecurrenceDto();
        BeanUtils.copyProperties(recurrenceEntity, recurrenceDto);


        return ResponseEntity.status(HttpStatus.CREATED).body(recurrenceDto);

    }
}
