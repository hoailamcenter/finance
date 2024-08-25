package com.finance.mapper;

import com.finance.dto.serviceGroup.ServiceGroupAdminDto;
import com.finance.dto.serviceGroup.ServiceGroupDto;
import com.finance.form.serviceGroup.CreateServiceGroupForm;
import com.finance.form.serviceGroup.UpdateServiceGroupForm;
import com.finance.model.ServiceGroup;
import org.mapstruct.*;

import java.util.List;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ServiceGroupMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToServiceGroupAdminDto")
    ServiceGroupAdminDto fromEntityToServiceGroupAdminDto(ServiceGroup serviceGroup);

    @IterableMapping(elementTargetType = ServiceGroupAdminDto.class, qualifiedByName = "fromEntityToServiceGroupAdminDto")
    List<ServiceGroupAdminDto> fromEntityToServiceGroupAdminDtoList(List<ServiceGroup> serviceGroups);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToServiceGroupDto")
    ServiceGroupDto fromEntityToServiceGroupDto(ServiceGroup serviceGroup);

    @IterableMapping(elementTargetType = ServiceGroupDto.class, qualifiedByName = "fromEntityToServiceGroupDto")
    List<ServiceGroupDto> fromEntityToServiceGroupDtoList(List<ServiceGroup> serviceGroups);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    ServiceGroup fromCreateServiceGroupFormToEntity(CreateServiceGroupForm createServiceGroupForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateServiceGroupFormToEntity(UpdateServiceGroupForm createServiceGroupForm, @MappingTarget ServiceGroup serviceGroup );
}
