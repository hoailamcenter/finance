package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.serviceSchedule.ServiceScheduleAdminDto;
import com.finance.dto.serviceSchedule.ServiceScheduleDto;
import com.finance.form.serviceSchedule.UpdateServiceScheduleForm;
import com.finance.mapper.ServiceScheduleMapper;
import com.finance.model.Service;
import com.finance.model.ServiceNotificationGroup;
import com.finance.model.ServiceSchedule;
import com.finance.model.criteria.ServiceScheduleCriteria;
import com.finance.repository.ServiceNotificationGroupRepository;
import com.finance.repository.ServiceRepository;
import com.finance.repository.ServiceScheduleRepository;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/service-schedule")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ServiceScheduleController extends ABasicController {
    @Autowired
    private ServiceNotificationGroupRepository serviceNotificationGroupRepository;
    @Autowired
    private ServiceScheduleRepository serviceScheduleRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceScheduleMapper serviceScheduleMapper;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_S_V')")
    public ApiMessageDto<ServiceScheduleAdminDto> get(@PathVariable("id") Long id) {
        ServiceSchedule serviceSchedule = serviceScheduleRepository.findById(id).orElse(null);
        if (serviceSchedule == null) {
            return makeErrorResponse(ErrorCode.SERVICE_SCHEDULE_ERROR_NOT_FOUND, "Not found service schedule");
        }
        return makeSuccessResponse(serviceScheduleMapper.fromEntityToServiceScheduleAdminDto(serviceSchedule), "Get service schedule success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_S_L')")
    public ApiMessageDto<ResponseListDto<List<ServiceScheduleAdminDto>>> list(ServiceScheduleCriteria serviceScheduleCriteria, Pageable pageable) {
        if (serviceScheduleCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<ServiceSchedule> serviceSchedules = serviceScheduleRepository.findAll(serviceScheduleCriteria.getCriteria(), pageable);
        ResponseListDto<List<ServiceScheduleAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(serviceScheduleMapper.fromEntityListToServiceScheduleAdminDtoList(serviceSchedules.getContent()));
        responseListObj.setTotalPages(serviceSchedules.getTotalPages());
        responseListObj.setTotalElements(serviceSchedules.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list service schedule success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ServiceScheduleDto>>> autoComplete(ServiceScheduleCriteria serviceScheduleCriteria) {
        Pageable pageable = serviceScheduleCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        serviceScheduleCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<ServiceSchedule> serviceSchedules = serviceScheduleRepository.findAll(serviceScheduleCriteria.getCriteria(), pageable);
        ResponseListDto<List<ServiceScheduleDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(serviceScheduleMapper.fromEntityListToServiceScheduleDtoList(serviceSchedules.getContent()));
        responseListObj.setTotalPages(serviceSchedules.getTotalPages());
        responseListObj.setTotalElements(serviceSchedules.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list service schedule success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_S_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateServiceScheduleForm updateServiceScheduleForm, BindingResult bindingResult) {
        Service service = serviceRepository.findById(updateServiceScheduleForm.getServiceId()).orElse(null);
        if (service == null) {
            return makeErrorResponse(ErrorCode.SERVICE_ERROR_NOT_FOUND, "Not found service");
        }
        serviceScheduleRepository.deleteAllByServiceId(service.getId());
        List<ServiceSchedule> serviceSchedules = new ArrayList<>();
        List<Integer> numberOfDueDaysList = updateServiceScheduleForm.getNumberOfDueDaysList();
        int ordering = 1;
        for (Integer numberOfDueDays : numberOfDueDaysList) {
            ServiceSchedule serviceSchedule = new ServiceSchedule();
            serviceSchedule.setService(service);
            serviceSchedule.setNumberOfDueDays(numberOfDueDays);
            serviceSchedule.setOrdering(ordering++);
            serviceSchedules.add(serviceSchedule);
        }
        serviceScheduleRepository.saveAll(serviceSchedules);
        return makeSuccessResponse(null, "Update service schedule ordering success");
    }
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_N_G_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        ServiceNotificationGroup serviceNotificationGroup = serviceNotificationGroupRepository.findById(id).orElse(null);
        if (serviceNotificationGroup == null){
            return makeErrorResponse(ErrorCode.SERVICE_NOTIFICATION_GROUP_ERROR_NOT_FOUND, "Not found service notification group");
        }
        serviceNotificationGroupRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete service notification group success");
    }
}

