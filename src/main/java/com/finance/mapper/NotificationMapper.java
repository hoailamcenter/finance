package com.finance.mapper;

import com.finance.dto.notification.NotificationAdminDto;
import com.finance.dto.notification.NotificationDto;
import com.finance.form.notification.CreateNotificationForm;
import com.finance.form.notification.UpdateNotificationForm;
import com.finance.model.Notification;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper {
    @Mapping(source = "message", target = "message")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "serviceId", target = "serviceId")
    @Mapping(source = "accountId", target = "accountId")
    @BeanMapping(ignoreByDefault = true)
    Notification fromCreateNotificationFormToEntity(CreateNotificationForm createNotificationForm);

    @Mapping(source = "message", target = "message")
    @Mapping(source = "state", target = "state")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateNotificationFormToEntity(UpdateNotificationForm updateNotificationForm, @MappingTarget Notification notification);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "serviceId", target = "serviceId")
    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToNotificationAdminDto")
    NotificationAdminDto fromEntityToNotificationAdminDto(Notification notification);

    @IterableMapping(elementTargetType = NotificationAdminDto.class, qualifiedByName = "fromEntityToNotificationAdminDto")
    List<NotificationAdminDto> fromEntityToNotificationAdminDtoList(List<Notification> notifications);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "serviceId", target = "serviceId")
    @Mapping(source = "accountId", target = "accountId")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToNotificationDto")
    NotificationDto fromEntityToNotificationDto(Notification notification);

    @IterableMapping(elementTargetType = NotificationDto.class, qualifiedByName = "fromEntityToNotificationDto")
    List<NotificationDto> fromEntityToNotificationDtoList(List<Notification> notifications);

}
