package io.github.LuizYokoyama.SchedularPayments.service;

import io.github.LuizYokoyama.SchedularPayments.dto.CreateRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.EditRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.RecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.entity.*;
import io.github.LuizYokoyama.SchedularPayments.exceptions.BadRequestRuntimeException;
import io.github.LuizYokoyama.SchedularPayments.exceptions.NotFoundRuntimeException;
import io.github.LuizYokoyama.SchedularPayments.exceptions.PreviousDateRuntimeException;
import io.github.LuizYokoyama.SchedularPayments.exceptions.ValueZeroRuntimeException;
import io.github.LuizYokoyama.SchedularPayments.repository.AccountRepository;
import io.github.LuizYokoyama.SchedularPayments.repository.EntryRepository;
import io.github.LuizYokoyama.SchedularPayments.repository.RecurrenceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public RecurrenceDto schedule(CreateRecurrenceDto createRecurrenceDto) {

        validateValueDate(createRecurrenceDto.getValue(), createRecurrenceDto.getOccurrenceDate());

        Optional<AccountEntity> accountEntityOptional = accountRepository.findById(createRecurrenceDto.getAccountId());
        if (accountEntityOptional.isEmpty()){
            throw new NotFoundRuntimeException("Conta não encontrada. Forneça uma conta válida.");
        }

        Optional<AccountEntity> accountDestinationEntityOptional = accountRepository.findById(createRecurrenceDto.getAccountDestinationID());
        if (accountDestinationEntityOptional.isEmpty()){
            throw new NotFoundRuntimeException("Conta de destino não encontrada. Forneça uma conta de destino válida.");
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

        return recurrenceDto;
    }

    @Transactional
    public RecurrenceDto editScheduled(UUID uuid, EditRecurrenceDto editRecurrenceDto) {

        validateValueDate(editRecurrenceDto.getValue(), editRecurrenceDto.getOccurrenceDate());

        RecurrenceEntity recurrenceEntity = validateRecurrence(uuid);

        if (recurrenceEntity.getValue() == editRecurrenceDto.getValue() &&
                recurrenceEntity.getDuration() == editRecurrenceDto.getDuration() &&
                recurrenceEntity.getOccurrenceDate().isEqual(editRecurrenceDto.getOccurrenceDate())){
            throw new BadRequestRuntimeException("Não há alteração nos dados desta recorrênca. Verifique se os dados estão corretos.");
        }

        Set<EntryEntity> entryEntitySet = recurrenceEntity.getEntrySet();

        for (EntryEntity entryEntity : entryEntitySet ){
            if (entryEntity.getEntryStatus().equals(EntryStatus.PENDING)){
                entryEntity.setEntryStatus(EntryStatus.CANCELED);
                entryEntity.setRecurrenceEntity(null);
            }
        }

        AccountEntity accountEntity = recurrenceEntity.getAccountEntity();
        AccountEntity accountDestinationEntity = recurrenceEntity.getAccountDestination();
        for (int i = 0; i < editRecurrenceDto.getDuration(); i++){

            LocalDateTime entryDate = editRecurrenceDto.getOccurrenceDate().plusMonths(i).atTime(0, 0);

            // CREDIT
            EntryEntity entryEntityToCredit = new EntryEntity();
            entryEntityToCredit.setRecurrenceEntity(recurrenceEntity);
            entryEntityToCredit.setAccountEntity(accountDestinationEntity); //will receive the amount
            entryEntityToCredit.setOriginEntity(accountEntity); //will send the amount
            entryEntityToCredit.setEntryDateTime(entryDate);
            entryEntityToCredit.setValue(editRecurrenceDto.getValue());
            entryEntityToCredit.setOperationType(OperationType.CREDIT);
            entryEntityToCredit.setEntryStatus(EntryStatus.PENDING);
            entryEntityToCredit = entryRepository.save(entryEntityToCredit);
            entryEntitySet.add(entryEntityToCredit);

            // DEBIT
            EntryEntity entryEntityToDebit = new EntryEntity();
            entryEntityToDebit.setRecurrenceEntity(recurrenceEntity);
            entryEntityToDebit.setAccountEntity(accountEntity); //will send the amount
            entryEntityToDebit.setOriginEntity(accountDestinationEntity); //will receive the amount
            entryEntityToDebit.setEntryDateTime(entryDate);
            entryEntityToDebit.setValue(editRecurrenceDto.getValue());
            entryEntityToDebit.setOperationType(OperationType.DEBIT);
            entryEntityToDebit.setEntryStatus(EntryStatus.PENDING);
            entryEntityToDebit = entryRepository.save(entryEntityToDebit);
            entryEntitySet.add(entryEntityToDebit);

        }

        recurrenceEntity.setValue(editRecurrenceDto.getValue());
        recurrenceEntity.setDuration(editRecurrenceDto.getDuration());
        recurrenceEntity.setOccurrenceDate(editRecurrenceDto.getOccurrenceDate());
        recurrenceEntity.setRecurrenceStatus(RecurrenceStatus.PENDING);
        recurrenceEntity = recurrenceRepository.save(recurrenceEntity);

        RecurrenceDto recurrenceDto = new RecurrenceDto();
        BeanUtils.copyProperties(recurrenceEntity, recurrenceDto);
        recurrenceDto.setAccountId(accountEntity.getAccountId());
        recurrenceDto.setAccountDestinationID(accountDestinationEntity.getAccountId());

        return recurrenceDto;

    }



    @Transactional
    public RecurrenceDto cancelScheduledPayment(UUID uuid) {

        RecurrenceEntity recurrenceEntity = validateRecurrence(uuid);

        Set<EntryEntity> entryEntitySet = recurrenceEntity.getEntrySet();

        for (EntryEntity entryEntity : entryEntitySet ){
            if (entryEntity.getEntryStatus().equals(EntryStatus.PENDING)){
                entryEntity.setEntryStatus(EntryStatus.CANCELED);
            }
        }

        recurrenceEntity.setRecurrenceStatus(RecurrenceStatus.CANCELED);
        recurrenceEntity = recurrenceRepository.save(recurrenceEntity);

        RecurrenceDto recurrenceDto = new RecurrenceDto();
        BeanUtils.copyProperties(recurrenceEntity, recurrenceDto);
        recurrenceDto.setAccountId(recurrenceEntity.getAccountEntity().getAccountId());
        recurrenceDto.setAccountDestinationID(recurrenceEntity.getAccountDestination().getAccountId());

        return recurrenceDto;

    }

    private RecurrenceEntity validateRecurrence(UUID uuid) {

        Optional<RecurrenceEntity> recurrenceEntityOptional = recurrenceRepository.findById(uuid);

        if (recurrenceEntityOptional.isEmpty()){
            throw new NotFoundRuntimeException("Recorrência não encontrada. Forneça uma recorrência válida.");
        }

        RecurrenceEntity recurrenceEntity = recurrenceEntityOptional.get();

        if (recurrenceEntity.getRecurrenceStatus() == RecurrenceStatus.CANCELED ){
            throw new BadRequestRuntimeException("Recorrência já cancelada e não pode ser editada. Verifique se esta recorrência está correta.");
        }

        if ( recurrenceEntity.getRecurrenceStatus() == RecurrenceStatus.DONE ){
            throw new BadRequestRuntimeException("Recorrência já concluída e não pode ser editada. Verifique se esta recorrência está correta.");
        }

        return recurrenceEntity;
    }

    private void validateValueDate(float value, LocalDate date){

        if (value == 0){
            throw new ValueZeroRuntimeException("Forneça um valor maior que zero!");
        }

        if (date.isBefore(LocalDate.now())){
            throw new PreviousDateRuntimeException("Forneça uma data presente ou futura!");
        }


    }
}
