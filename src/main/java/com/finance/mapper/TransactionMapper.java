package com.finance.mapper;

import com.finance.dto.account.KeyWrapperDto;
import com.finance.dto.transaction.MyTransactionDto;
import com.finance.dto.transaction.TransactionAdminDto;
import com.finance.dto.transaction.TransactionDto;
import com.finance.form.transaction.CreateTransactionForm;
import com.finance.form.transaction.UpdateTransactionForm;
import com.finance.model.Transaction;
import org.mapstruct.*;

import java.util.List;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CategoryMapper.class})
public interface TransactionMapper {

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "document", target = "document")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "transactionDate", target = "transactionDate")
    @BeanMapping(ignoreByDefault = true)
    Transaction fromCreateTransactionFormToEntity(CreateTransactionForm createTransactionForm);

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "document", target = "document")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "transactionGroupId", target = "transactionGroup.id")
    @Mapping(source = "transactionDate", target = "transactionDate")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateTransactionFormToEntity(UpdateTransactionForm updateTransactionForm, @MappingTarget Transaction transaction);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "category", target = "category", qualifiedByName = "fromEntityToCategoryDto")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "document", target = "document")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "transactionDate", target = "transactionDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTransactionAdminDto")
    TransactionAdminDto fromEntityToTransactionAdminDto(Transaction transaction);

    @IterableMapping(elementTargetType = TransactionAdminDto.class, qualifiedByName = "fromEntityToTransactionAdminDto")
    List<TransactionAdminDto> fromEntityListToTransactionAdminDtoList(List<Transaction> transactions);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "category", target = "category", qualifiedByName = "fromEntityToCategoryDto")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "document", target = "document")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTransactionDto")
    TransactionDto fromEntityToTransactionDto(Transaction transaction);

    @IterableMapping(elementTargetType = TransactionDto.class, qualifiedByName = "fromEntityToTransactionDto")
    List<TransactionDto> fromEntityListToTransactionDtoList(List<Transaction> transactions);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "transactionDate", target = "transactionDate")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "categoryName", target = "category.name")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "note", target = "note")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromMyTransactionDtoToTransactionDto")
    TransactionDto fromMyTransactionDtoToTransactionDto(MyTransactionDto myTransactionDto);

    @IterableMapping(elementTargetType = TransactionDto.class, qualifiedByName = "fromMyTransactionDtoToTransactionDto")
    List<TransactionDto> fromMyTransactionDtoListToTransactionDtoList(List<MyTransactionDto> myTransactionDtos);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTransactionDtoAutoComplete")
    TransactionDto fromEntityToTransactionDtoAutoComplete(Transaction transaction);

    @IterableMapping(elementTargetType = TransactionDto.class, qualifiedByName = "fromEntityToTransactionDtoAutoComplete")
    List<TransactionDto> fromEntityListToTransactionDtoAutoCompleteList(List<Transaction> transactions);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "category", target = "category", qualifiedByName = "fromEntityToCategoryDto")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "document", target = "document")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTransactionDtoForTransactionGroup")
    TransactionDto fromEntityToTransactionDtoForTransactionGroup(Transaction transaction);

    @IterableMapping(elementTargetType = TransactionDto.class, qualifiedByName = "fromEntityToTransactionDtoForTransactionGroup")
    List<TransactionDto> fromEntityListToTransactionDtoListForTransactionGroup(List<Transaction> transactions);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "category", target = "category", qualifiedByName = "fromEntityToCategoryDto")
    @Mapping(source = "transactionGroup", target = "transactionGroup", qualifiedByName = "fromEntityToTransactionGroupDto")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "document", target = "document")
    @Mapping(source = "transactionDate", target = "transactionDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTransactionDtoForPaymentPeriod")
    TransactionDto fromEntityToTransactionDtoForPaymentPeriod(Transaction transaction);

    @IterableMapping(elementTargetType = TransactionDto.class, qualifiedByName = "fromEntityToTransactionDtoForPaymentPeriod")
    List<TransactionDto> fromEntityListToTransactionDtoListForPaymentPeriod(List<Transaction> transactions);

}
