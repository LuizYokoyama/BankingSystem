package io.github.LuizYokoyama.SchedularPayments.service;

import io.github.LuizYokoyama.SchedularPayments.dto.CreateRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.EditRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.IRecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.dto.RecurrenceDto;
import io.github.LuizYokoyama.SchedularPayments.entity.*;
import io.github.LuizYokoyama.SchedularPayments.exceptions.*;
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

        validateRecurrence(createRecurrenceDto);

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
        try {
            recurrenceEntity = recurrenceRepository.save(recurrenceEntity);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao salvar a recorrência!", ex.getCause());
        }

        Set<EntryEntity> entrySet = new HashSet<>();
        generateNewEntries(entrySet, createRecurrenceDto, recurrenceEntity);

        recurrenceEntity.setEntrySet(entrySet);
        try {
            recurrenceEntity = recurrenceRepository.save(recurrenceEntity);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao salvar a recorrência!", ex.getCause());
        }

        RecurrenceDto recurrenceDto = new RecurrenceDto();
        BeanUtils.copyProperties(recurrenceEntity, recurrenceDto);
        recurrenceDto.setAccountId(createRecurrenceDto.getAccountId());
        recurrenceDto.setAccountDestinationID(createRecurrenceDto.getAccountDestinationID());

        return recurrenceDto;
    }

    @Transactional
    public RecurrenceDto editScheduled(UUID uuid, EditRecurrenceDto editRecurrenceDto) {

        validateRecurrence(editRecurrenceDto);

        RecurrenceEntity recurrenceEntity = GetValidRecurrence(uuid);

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

        generateNewEntries(entryEntitySet, editRecurrenceDto, recurrenceEntity);

        recurrenceEntity.setValue(editRecurrenceDto.getValue());
        recurrenceEntity.setDuration(editRecurrenceDto.getDuration());
        recurrenceEntity.setOccurrenceDate(editRecurrenceDto.getOccurrenceDate());
        recurrenceEntity.setRecurrenceStatus(RecurrenceStatus.PENDING);
        try {
            recurrenceEntity = recurrenceRepository.save(recurrenceEntity);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao salvar a recorrência!", ex.getCause());
        }

        RecurrenceDto recurrenceDto = new RecurrenceDto();
        BeanUtils.copyProperties(recurrenceEntity, recurrenceDto);
        recurrenceDto.setAccountId(recurrenceEntity.getAccountEntity().getAccountId());
        recurrenceDto.setAccountDestinationID(recurrenceEntity.getAccountDestination().getAccountId());

        return recurrenceDto;

    }


    @Transactional
    public RecurrenceDto cancelScheduledPayment(UUID uuid) {

        RecurrenceEntity recurrenceEntity = GetValidRecurrence(uuid);

        Set<EntryEntity> entryEntitySet = recurrenceEntity.getEntrySet();

        for (EntryEntity entryEntity : entryEntitySet ){
            if (entryEntity.getEntryStatus().equals(EntryStatus.PENDING)){
                entryEntity.setEntryStatus(EntryStatus.CANCELED);
            }
        }

        recurrenceEntity.setRecurrenceStatus(RecurrenceStatus.CANCELED);
        try {
            recurrenceEntity = recurrenceRepository.save(recurrenceEntity);
        }catch (Exception ex){
            throw new DataBaseException("Falha ao salvar a recorrência!", ex.getCause());
        }

        RecurrenceDto recurrenceDto = new RecurrenceDto();
        BeanUtils.copyProperties(recurrenceEntity, recurrenceDto);
        recurrenceDto.setAccountId(recurrenceEntity.getAccountEntity().getAccountId());
        recurrenceDto.setAccountDestinationID(recurrenceEntity.getAccountDestination().getAccountId());

        return recurrenceDto;

    }

    private void generateNewEntries(Set<EntryEntity> entrySet, IRecurrenceDto recurrenceDto, RecurrenceEntity recurrenceEntity){

        AccountEntity accountEntity = recurrenceEntity.getAccountEntity();
        AccountEntity accountDestinationEntity = recurrenceEntity.getAccountDestination();

        for (int i = 0; i < recurrenceDto.getDuration(); i++){

            LocalDateTime entryDate = recurrenceDto.getOccurrenceDate().plusMonths(i).atTime(0, 0);

            // CREDIT
            EntryEntity entryEntityToCredit = new EntryEntity();
            entryEntityToCredit.setRecurrenceEntity(recurrenceEntity);
            entryEntityToCredit.setAccountEntity(accountDestinationEntity); //will receive the amount
            entryEntityToCredit.setOriginEntity(accountEntity); //will send the amount
            entryEntityToCredit.setEntryDateTime(entryDate);
            entryEntityToCredit.setValue(recurrenceDto.getValue());
            entryEntityToCredit.setOperationType(OperationType.CREDIT);
            entryEntityToCredit.setEntryStatus(EntryStatus.PENDING);
            try {
                entryEntityToCredit = entryRepository.save(entryEntityToCredit);
            }catch (Exception ex){
                throw new DataBaseException("Falha ao salvar entrada de crédito!", ex.getCause());
            }
            entrySet.add(entryEntityToCredit);

            // DEBIT
            EntryEntity entryEntityToDebit = new EntryEntity();
            entryEntityToDebit.setRecurrenceEntity(recurrenceEntity);
            entryEntityToDebit.setAccountEntity(accountEntity); //will send the amount
            entryEntityToDebit.setOriginEntity(accountDestinationEntity); //will receive the amount
            entryEntityToDebit.setEntryDateTime(entryDate);
            entryEntityToDebit.setValue(recurrenceDto.getValue());
            entryEntityToDebit.setOperationType(OperationType.DEBIT);
            entryEntityToDebit.setEntryStatus(EntryStatus.PENDING);
            try {
                entryEntityToDebit = entryRepository.save(entryEntityToDebit);
            }catch (Exception ex){
                throw new DataBaseException("Falha ao salvar entrada de débito!", ex.getCause());
            }
            entrySet.add(entryEntityToDebit);
        }

    }

    private RecurrenceEntity GetValidRecurrence(UUID uuid) {

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

    private void validateRecurrence(IRecurrenceDto recurrence){

        if (recurrence.getValue() == 0){
            throw new ValueZeroRuntimeException("Forneça um valor maior que zero!");
        }

        if (recurrence.getOccurrenceDate().isBefore(LocalDate.now())){
            throw new PreviousDateRuntimeException("Forneça uma data presente ou futura!");
        }

        if (recurrence.getClass().equals(CreateRecurrenceDto.class)){
            if (((CreateRecurrenceDto) recurrence).getAccountDestinationID() == ((CreateRecurrenceDto) recurrence).getAccountId()){
                throw new BadRequestRuntimeException("Forneça uma conta de destino diferente desta conta");
            }
        }

    }
}
