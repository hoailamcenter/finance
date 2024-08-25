package com.finance.mapper;

import org.mapstruct.Mapper;
import com.finance.dto.transactionHistory.TransactionHistoryAdminDto;
import com.finance.dto.transactionHistory.TransactionHistoryDto;
import com.finance.form.transactionHistory.UpdateTransactionHistoryForm;
import com.finance.model.TransactionHistory;
import org.mapstruct.*;

import java.util.List;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class, TransactionMapper.class})
public interface TransactionHistoryMapper {
    @Mapping(source = "note", target = "note")
    @Mapping(source = "state", target = "state")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateTransactionHistoryFormToEntity(UpdateTransactionHistoryForm updateTransactionHistoryForm, @MappingTarget TransactionHistory transactionHistory);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTransactionHistoryDto")
    TransactionHistoryDto fromEntityToTransactionHistoryDto(TransactionHistory transactionHistory);

    @IterableMapping(elementTargetType = TransactionHistoryDto.class, qualifiedByName = "fromEntityToTransactionHistoryDto")
    List<TransactionHistoryDto> fromEntityListToTransactionHistoryDtoList(List<TransactionHistory> transactionHistories);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoForNotificationGroup")
    @Mapping(source = "transaction", target = "transaction", qualifiedByName = "fromEntityToTransactionDto")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "note", target = "note")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTransactionHistoryAdminDto")
    TransactionHistoryAdminDto fromEntityToTransactionHistoryAdminDto(TransactionHistory transactionHistory);

    @IterableMapping(elementTargetType = TransactionHistoryAdminDto.class, qualifiedByName = "fromEntityToTransactionHistoryAdminDto")
    List<TransactionHistoryAdminDto> fromEntityListToTransactionHistoryAdminDtoList(List<TransactionHistory> transactionHistories);
}
