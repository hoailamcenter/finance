package com.finance.mapper;

import com.finance.dto.category.CategoryDto;
import com.finance.dto.notificationGroup.NotificationGroupAdminDto;
import com.finance.dto.notificationGroup.NotificationGroupDto;
import com.finance.form.notificationGroup.CreateNotificationGroupForm;
import com.finance.form.notificationGroup.UpdateNotificationGroupForm;
import com.finance.model.NotificationGroup;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationGroupMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    NotificationGroup fromCreateNotificationGroupFormToEntity(CreateNotificationGroupForm createNotificationGroupForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateGroupFormToEntity(UpdateNotificationGroupForm updateNotificationGroupForm, @MappingTarget NotificationGroup notificationGroup);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToNotificationGroupAdminDto")
    NotificationGroupAdminDto fromEntityToNotificationGroupAdminDto(NotificationGroup notificationGroup);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToNotificationGroupDto")
    NotificationGroupDto fromEntityToNotificationGroupDto(NotificationGroup notificationGroup);

    @IterableMapping(elementTargetType = NotificationGroupAdminDto.class, qualifiedByName = "fromEntityToNotificationGroupAdminDto")
    List<NotificationGroupAdminDto> fromEntityToNotificationGroupDtoList(List<NotificationGroup> notificationGroupList);

    @IterableMapping(elementTargetType = NotificationGroupDto.class, qualifiedByName = "fromEntityToNotificationGroupDto")
    List<NotificationGroupDto> fromNotificationGroupDtoList(List<NotificationGroup> notificationGroupList);

}
