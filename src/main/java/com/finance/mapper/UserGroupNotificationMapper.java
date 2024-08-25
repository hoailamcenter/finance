package com.finance.mapper;


import com.finance.dto.userGroupNotification.UserGroupNotificationAdminDto;
import com.finance.dto.userGroupNotification.UserGroupNotificationDto;
import com.finance.model.UserGroupNotification;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class, NotificationGroupMapper.class} )
public interface UserGroupNotificationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoForNotificationGroup")
    @Mapping(source = "notificationGroup", target = "notificationGroup", qualifiedByName = "fromEntityToNotificationGroupDto")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToUserGroupNotificationAdminDto")
    UserGroupNotificationAdminDto fromEntityToUserGroupNotificationAdminDto(UserGroupNotification userGroupNotification);

    @IterableMapping(elementTargetType = UserGroupNotificationAdminDto.class, qualifiedByName = "fromEntityToUserGroupNotificationAdminDto")
    List<UserGroupNotificationAdminDto> fromEntityListToUserGroupNotificationAdminDtoList(List<UserGroupNotification> userGroupNotifications);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoForNotificationGroup")
    @Mapping(source = "notificationGroup", target = "notificationGroup", qualifiedByName = "fromEntityToNotificationGroupDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToUserGroupNotificationDto")
    UserGroupNotificationDto fromEntityToUserGroupNotificationDto(UserGroupNotification userGroupNotification);

    @IterableMapping(elementTargetType = UserGroupNotificationDto.class, qualifiedByName = "fromEntityToUserGroupNotificationDto")
    List<UserGroupNotificationDto> fromEntityListToUserGroupNotificationDtoList(List<UserGroupNotification> userGroupNotifications);
}
