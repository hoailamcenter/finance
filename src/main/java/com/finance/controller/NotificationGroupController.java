package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.notificationGroup.NotificationGroupAdminDto;
import com.finance.dto.notificationGroup.NotificationGroupDto;
import com.finance.form.notificationGroup.CreateNotificationGroupForm;
import com.finance.form.notificationGroup.UpdateNotificationGroupForm;
import com.finance.mapper.NotificationGroupMapper;
import com.finance.model.NotificationGroup;
import com.finance.model.criteria.NotificationGroupCriteria;
import com.finance.repository.NotificationGroupRepository;
import com.finance.repository.ServiceNotificationGroupRepository;
import com.finance.repository.UserGroupNotificationRepository;
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
@RequestMapping("/v1/notification-group")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NotificationGroupController extends ABasicController{
    @Autowired
    private NotificationGroupRepository notificationGroupRepository;
    @Autowired
    private ServiceNotificationGroupRepository serviceNotificationGroupRepository;
    @Autowired
    private UserGroupNotificationRepository userGroupNotificationRepository;
    @Autowired
    private NotificationGroupMapper notificationGroupMapper;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_V')")
    public ApiMessageDto<NotificationGroupAdminDto> get(@PathVariable("id") Long id) {
        NotificationGroup notificationGroup = notificationGroupRepository.findById(id).orElse(null);
        if (notificationGroup == null) {
            return makeErrorResponse(ErrorCode.NOTIFICATION_ERROR_NOT_FOUND, "Not found notification group");
        }
        return makeSuccessResponse(notificationGroupMapper.fromEntityToNotificationGroupAdminDto(notificationGroup), "Get notification success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_L')")
    public ApiMessageDto<ResponseListDto<List<NotificationGroupDto>>> list(NotificationGroupCriteria notificationGroupCriteria, Pageable pageable) {
        if (notificationGroupCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<NotificationGroup> notificationGroups = notificationGroupRepository.findAll(notificationGroupCriteria.getCriteria(), pageable);
        ResponseListDto<List<NotificationGroupDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(notificationGroupMapper.fromNotificationGroupDtoList(notificationGroups.getContent()));
        responseListObj.setTotalPages(notificationGroups.getTotalPages());
        responseListObj.setTotalElements(notificationGroups.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list notification success");
    }
    @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_G_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateNotificationGroupForm createNotificationGroupForm, BindingResult bindingResult) {
        NotificationGroup notificationGroup = notificationGroupMapper.fromCreateNotificationGroupFormToEntity(createNotificationGroupForm);
        NotificationGroup notificationGroupByName = notificationGroupRepository.findFirstByName(notificationGroup.getName()).orElse(null);
        if (notificationGroupByName != null){
            return makeErrorResponse(ErrorCode.NOTIFICATION_GROUP_ERROR_NAME_EXISTED, "Notification group name existed");
        }
        notificationGroupRepository.save(notificationGroup);
        return makeSuccessResponse(null, "Create notification group success");
    }

    @PutMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_G_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateNotificationGroupForm updateNotificationGroupForm, BindingResult bindingResult) {
        NotificationGroup notificationGroup = notificationGroupRepository.findById(updateNotificationGroupForm.getId()).orElse(null);
        if (notificationGroup == null) {
            return makeErrorResponse(ErrorCode.NOTIFICATION_GROUP_ERROR_NOT_FOUND, "Not found notification group");
        }
        if (notificationGroup.getName() != null) {
            NotificationGroup notificationGroupByName = notificationGroupRepository.findFirstByName(notificationGroup.getName()).orElse(null);
            if (notificationGroupByName != null){
                return makeErrorResponse(ErrorCode.NOTIFICATION_GROUP_ERROR_NAME_EXISTED, "Notification group name existed");
            }
        }
        notificationGroupMapper.fromUpdateGroupFormToEntity(updateNotificationGroupForm, notificationGroup);
        notificationGroupRepository.save(notificationGroup);
        return makeSuccessResponse(null, "Update notification group success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NO_G_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        NotificationGroup notificationGroup = notificationGroupRepository.findById(id).orElse(null);
        if (notificationGroup == null){
            return makeErrorResponse(ErrorCode.NOTIFICATION_GROUP_ERROR_NOT_FOUND, "Not found notification group");
        }
        userGroupNotificationRepository.deleteAllByNotificationGroupId(id);
        serviceNotificationGroupRepository.deleteAllByNotificationGroupId(id);
        notificationGroupRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete notification service success");
    }
}
