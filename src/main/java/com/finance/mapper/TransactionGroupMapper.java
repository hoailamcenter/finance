package com.finance.mapper;

import com.finance.dto.transactionGroup.TransactionGroupAdminDto;
import com.finance.dto.transactionGroup.TransactionGroupDto;
import com.finance.form.transactionGroup.CreateTransactionGroupForm;
import com.finance.form.transactionGroup.UpdateTransactionGroupForm;
import com.finance.model.TransactionGroup;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionGroupMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTransactionGroupAdminDto")
    TransactionGroupAdminDto fromEntityToTransactionGroupAdminDto(TransactionGroup transactionGroup);

    @IterableMapping(elementTargetType = TransactionGroupAdminDto.class, qualifiedByName = "fromEntityToTransactionGroupAdminDto")
    List<TransactionGroupAdminDto> fromEntityToTransactionGroupAdminDtoList(List<TransactionGroup> transactionGroups);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToTransactionGroupDto")
    TransactionGroupDto fromEntityToTransactionGroupDto(TransactionGroup transactionGroup);

    @IterableMapping(elementTargetType = TransactionGroupDto.class, qualifiedByName = "fromEntityToTransactionGroupDto")
    List<TransactionGroupDto> fromEntityListToTransactionGroupDtoList(List<TransactionGroup> transactionGroups);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    TransactionGroup fromCreateTransactionGroupFormToEntity(CreateTransactionGroupForm createTransactionGroupForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateTransactionGroupAdminDtoToEntity(UpdateTransactionGroupForm updateTransactionGroupForm, @MappingTarget TransactionGroup transactionGroup);
}
