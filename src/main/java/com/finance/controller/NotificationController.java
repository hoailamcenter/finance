package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.notification.NotificationAdminDto;
import com.finance.dto.notification.NotificationDto;
import com.finance.form.notification.CreateNotificationForm;
import com.finance.form.notification.UpdateNotificationForm;
import com.finance.mapper.NotificationMapper;
import com.finance.model.Notification;
import com.finance.model.criteria.NotificationCriteria;
import com.finance.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/notification")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NotificationController extends ABasicController{
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMapper  notificationMapper;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_V')")
    public ApiMessageDto<NotificationDto> get(@PathVariable("id") Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
            return makeErrorResponse(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND, "Not found notification");
        }
        return makeSuccessResponse(notificationMapper.fromEntityToNotificationDto(notification), "Get notification success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_L')")
    public ApiMessageDto<ResponseListDto<List<NotificationAdminDto>>> list(NotificationCriteria notificationCriteria, Pageable pageable) {
        if (notificationCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<Notification> notifications = notificationRepository.findAll(notificationCriteria.getCriteria(), pageable);
        ResponseListDto<List<NotificationAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(notificationMapper.fromEntityToNotificationAdminDtoList(notifications.getContent()));
        responseListObj.setTotalPages(notifications.getTotalPages());
        responseListObj.setTotalElements(notifications.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list notification success");
    }
    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<NotificationDto>>> autoComplete(NotificationCriteria notificationCriteria) {
        Pageable pageable = notificationCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        notificationCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<Notification> notifications = notificationRepository.findAll(notificationCriteria.getCriteria(), pageable);
        ResponseListDto<List<NotificationDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(notificationMapper.fromEntityToNotificationDtoList(notifications.getContent()));
        responseListObj.setTotalPages(notifications.getTotalPages());
        responseListObj.setTotalElements(notifications.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list notification success");
    }
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateNotificationForm createNotificationForm, BindingResult bindingResult){
        Notification notification = notificationMapper.fromCreateNotificationFormToEntity(createNotificationForm);
        notificationRepository.save(notification);
        return makeSuccessResponse(null, "Create notification success");
    }
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateNotificationForm updateNotificationForm, BindingResult bindingResult) {
        Notification notification = notificationRepository.findById(updateNotificationForm.getId()).orElse(null);
        if (notification == null) {
            return makeErrorResponse(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND, "Not found notification");
        }
        notificationMapper.fromUpdateNotificationFormToEntity(updateNotificationForm, notification);
        notificationRepository.save(notification);
        return makeSuccessResponse(null, "Update notification success");
    }
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
            return makeErrorResponse(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND, "Not found notification");
        }
        notificationRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete notification success");
    }
}
