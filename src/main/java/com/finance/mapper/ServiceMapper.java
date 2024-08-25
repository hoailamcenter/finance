package com.finance.mapper;


import com.finance.dto.service.ServiceAdminDto;
import com.finance.dto.service.ServiceDto;
import com.finance.form.service.CreateServiceForm;
import com.finance.form.service.UpdateServiceForm;
import com.finance.model.Service;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,uses = {CategoryMapper.class})
public interface ServiceMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "periodKind", target = "periodKind")
    @Mapping(source = "expirationDate", target = "expirationDate")
    @Mapping(source = "category", target = "category", qualifiedByName = "fromEntityToCategoryDto")
    //@Mapping(source = "serviceGroup", target = "serviceGroup")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToServiceDto")
    ServiceDto fromEntityToServiceDto(Service service);

    @IterableMapping(elementTargetType = ServiceDto.class, qualifiedByName = "fromEntityToServiceDto")
    List<ServiceDto> fromEntityListToServiceDtoList(List<Service> services);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "periodKind", target = "periodKind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "expirationDate", target = "expirationDate")
    @Mapping(source = "category", target = "category", qualifiedByName = "fromEntityToCategoryDto")
    //@Mapping(source = "serviceGroup", target = "serviceGroup")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToServiceDto")
    ServiceAdminDto fromEntityToServiceAdminDto(Service service);

    @IterableMapping(elementTargetType = ServiceAdminDto.class, qualifiedByName = "fromEntityToCategoryAdminDto")
    List<ServiceAdminDto> fromEntityListToServiceAdminDtoList(List<Service> services);

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "periodKind", target = "periodKind")
    @Mapping(source = "expirationDate", target = "expirationDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCreateServiceFormToEntity")
    Service fromCreateServiceFormToEntity(CreateServiceForm createServiceForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "periodKind", target = "periodKind")
    @Mapping(source = "expirationDate", target = "expirationDate")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "serviceGroupId", target = "serviceGroup.id")
    void fromUpdateServiceFromToEntity(UpdateServiceForm updateServiceForm, @MappingTarget Service service);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "category", target = "category", qualifiedByName = "fromEntityToCategoryDto")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "money", target = "money")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToServiceDtoForServiceGroup")
    ServiceDto fromEntityToServiceDtoForServiceGroup(Service service);

    @IterableMapping(elementTargetType = ServiceDto.class, qualifiedByName = "fromEntityToServiceDtoForServiceGroup")
    List<ServiceDto> fromEntityListToServiceDtoListForServiceGroup(List<Service> services);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "money", target = "money")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToServiceDtoForNotificationGroup")
    ServiceDto fromEntityToServiceDtoForNotificationGroup(Service service);

}
