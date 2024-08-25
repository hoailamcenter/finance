package com.finance.mapper;

import com.finance.dto.serviceNotificationGroup.ServiceNotificationGroupAdminDto;
import com.finance.dto.serviceNotificationGroup.ServiceNotificationGroupDto;
import com.finance.model.ServiceNotificationGroup;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ServiceMapper.class, NotificationGroupMapper.class} )
public interface ServiceNotificationGroupMapper{

    @Mapping(source = "id", target = "id")
    @Mapping(source = "service", target = "service", qualifiedByName = "fromEntityToServiceDtoForNotificationGroup")
    @Mapping(source = "notificationGroup", target = "notificationGroup", qualifiedByName = "fromEntityToNotificationGroupDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToServiceNotificationGroupDto")
    ServiceNotificationGroupDto fromEntityToServiceNotificationGroupDto(ServiceNotificationGroup serviceNotificationGroup);

    @IterableMapping(elementTargetType = ServiceNotificationGroupDto.class, qualifiedByName = "fromEntityToServiceNotificationGroupDto")
    List<ServiceNotificationGroupDto> fromEntityListToServiceNotificationGroupDtoList(List<ServiceNotificationGroup> userGroupNotifications);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "service", target = "service", qualifiedByName = "fromEntityToServiceDtoForNotificationGroup")
    @Mapping(source = "notificationGroup", target = "notificationGroup", qualifiedByName = "fromEntityToNotificationGroupDto")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToServiceNotificationGroupAdminDto")
    ServiceNotificationGroupAdminDto fromEntityToServiceNotificationGroupAdminDto(ServiceNotificationGroup serviceNotificationGroup);

    @IterableMapping(elementTargetType = ServiceNotificationGroupAdminDto.class, qualifiedByName = "fromEntityToServiceNotificationGroupAdminDto")
    List<ServiceNotificationGroupAdminDto> fromEntityListToServiceNotificationGroupAdminDtoList(List<ServiceNotificationGroup> serviceNotificationGroups);
}

