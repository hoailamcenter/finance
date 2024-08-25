package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.service.ServiceAdminDto;
import com.finance.form.service.CreateServiceForm;
import com.finance.form.service.UpdateServiceForm;
import com.finance.mapper.ServiceMapper;
import com.finance.model.Category;
import com.finance.model.Service;
import com.finance.model.ServiceGroup;
import com.finance.model.criteria.ServiceCriteria;
import com.finance.repository.*;
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
@RequestMapping("v1/service")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ServiceController extends ABasicController{
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceMapper serviceMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ServiceGroupRepository serviceGroupRepository;
    @Autowired
    private ServiceScheduleRepository serviceScheduleRepository;
    @Autowired
    private ServiceNotificationGroupRepository serviceNotificationGroupRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_L')")
    public ApiMessageDto<ResponseListDto<List<ServiceAdminDto>>> list(ServiceCriteria serviceCriteria, Pageable pageable) {
        Page<Service> services = serviceRepository.findAll(serviceCriteria.getCriteria(), pageable);
        ResponseListDto<List<ServiceAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(serviceMapper.fromEntityListToServiceAdminDtoList(services.getContent()));
        responseListObj.setTotalPages(services.getTotalPages());
        responseListObj.setTotalElements(services.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list service success");
    }
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_V')")
    public ApiMessageDto<ServiceAdminDto> get(@PathVariable Long id) {
        Service service = serviceRepository.findById(id).orElse(null);
        if (service == null) {
            makeErrorResponse(ErrorCode.SERVICE_ERROR_NOT_FOUND, "Not found service");
        }
        return makeSuccessResponse(serviceMapper.fromEntityToServiceAdminDto(service), "Get service success");
    }
    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ServiceAdminDto>>> autoComplete(ServiceCriteria serviceCriteria) {
        Pageable pageable = PageRequest.of(0,10);
        serviceCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<Service> services = serviceRepository.findAll(serviceCriteria.getCriteria(), pageable);
        ResponseListDto<List<ServiceAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(serviceMapper.fromEntityListToServiceAdminDtoList(services.getContent()));
        responseListObj.setTotalPages(services.getTotalPages());
        responseListObj.setTotalElements(services.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list transaction success");
    }
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateServiceForm createServiceForm, BindingResult bindingResult){
        Service serviceByName = serviceRepository.findFirstByNameAndKind(createServiceForm.getName(),createServiceForm.getKind()).orElse(null);
        if (serviceByName != null) {
            makeErrorResponse(ErrorCode.SERVICE_ERROR_NAME_EXISTED, "Name already existed");
        }
        if (createServiceForm.getPeriodKind().equals(FinanceConstant.SERVICE_PERIOD_KIND_FIX_DAY) && createServiceForm.getStartDate().after(createServiceForm.getExpirationDate())){
            return makeErrorResponse(ErrorCode.SERVICE_ERROR_DATE_INVALID, "Start date must be before expiration date");
        }
        Service service = serviceMapper.fromCreateServiceFormToEntity(createServiceForm);
        if (createServiceForm.getServiceGroupId() != null) {
            ServiceGroup serviceGroup = serviceGroupRepository.findById(createServiceForm.getServiceGroupId()).orElse(null);
            if (serviceGroup == null){
                return makeErrorResponse(ErrorCode.SERVICE_GROUP_ERROR_NOT_FOUND, "Not found service group");
            }
            service.setServiceGroup(serviceGroup);
        }
        if (createServiceForm.getCategoryId() != null) {
            Category category = categoryRepository.findById(createServiceForm.getCategoryId()).orElse(null);
            if (category == null) {
                return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not found category");
            }
            if (!createServiceForm.getKind().equals(category.getKind())) {
                return makeErrorResponse(ErrorCode.SERVICE_ERROR_KIND_INVALID, "Service kind not match category");
            }
            service.setCategory(category);
        };
        service.setStatus(FinanceConstant.STATUS_ACTIVE);
        serviceRepository.save(service);
        return makeSuccessResponse(null,"create service success");
    }
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody  UpdateServiceForm updateServiceForm, BindingResult bindingResult){
        Service existedService = serviceRepository.findById(updateServiceForm.getId()).orElse(null);
        if (existedService == null) {
            return makeErrorResponse(ErrorCode.SERVICE_ERROR_NOT_FOUND, "Not found service");
        }
        if (existedService.getName() != null) {
            Service serviceByName = serviceRepository.findFirstByNameAndKind(updateServiceForm.getName(),updateServiceForm.getKind()).orElse(null);
            if (serviceByName != null) {
                return makeErrorResponse(ErrorCode.SERVICE_ERROR_NAME_EXISTED, "Name already existed");
            }
        }
        if (updateServiceForm.getPeriodKind().equals(FinanceConstant.SERVICE_PERIOD_KIND_FIX_DAY) && updateServiceForm.getStartDate().after(updateServiceForm.getExpirationDate())) {
            return makeErrorResponse(ErrorCode.SERVICE_ERROR_DATE_INVALID, "Start date must be before expiration date");
        }
        serviceMapper.fromUpdateServiceFromToEntity(updateServiceForm, existedService);
        if (updateServiceForm.getServiceGroupId() != null) {
            ServiceGroup serviceGroup = serviceGroupRepository.findById(updateServiceForm.getServiceGroupId()).orElse(null);
            if (serviceGroup == null){
                return makeErrorResponse(ErrorCode.SERVICE_GROUP_ERROR_NOT_FOUND, "Not found service group");
            }
            existedService.setServiceGroup(serviceGroup);
        } else {
            existedService.setServiceGroup(null);
        }
        if (updateServiceForm.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateServiceForm.getCategoryId()).orElse(null);
            if (category == null) {
                return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not found category");
            }
            if (!updateServiceForm.getKind().equals(category.getKind())) {
                return makeErrorResponse(ErrorCode.SERVICE_ERROR_KIND_INVALID, "Service kind not match category");
            }
            existedService.setCategory(category);
        } else {
            existedService.setCategory(null);
        }
        serviceRepository.save(existedService);
        return makeSuccessResponse(null, "Update service success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Service service = serviceRepository.findById(id).orElse(null);
        if(service == null){
            return makeErrorResponse(ErrorCode.SERVICE_ERROR_NOT_FOUND, "Not found service");
        }
        serviceScheduleRepository.deleteAllByServiceId(id);
        serviceNotificationGroupRepository.deleteAllByServiceId(id);
        serviceRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete service success");
    }
}
