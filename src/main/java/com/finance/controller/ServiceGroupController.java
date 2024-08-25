package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.serviceGroup.ServiceGroupAdminDto;
import com.finance.dto.serviceGroup.ServiceGroupDto;
import com.finance.form.serviceGroup.CreateServiceGroupForm;
import com.finance.form.serviceGroup.UpdateServiceGroupForm;
import com.finance.mapper.ServiceGroupMapper;
import com.finance.mapper.ServiceMapper;
import com.finance.model.Service;
import com.finance.model.ServiceGroup;
import com.finance.model.criteria.ServiceGroupCriteria;
import com.finance.repository.ServiceGroupRepository;
import com.finance.repository.ServiceRepository;
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
@RequestMapping("v1/service-group")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ServiceGroupController extends ABasicController{
    @Autowired
    private ServiceGroupRepository serviceGroupRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ServiceGroupMapper serviceGroupMapper;
    @Autowired
    private ServiceMapper serviceMapper;

    @GetMapping(value = "/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_G_V')")
    public ApiMessageDto<ServiceGroupAdminDto> get(@PathVariable("id")  Long id) {
        ServiceGroup serviceGroup = serviceGroupRepository.findById(id).orElse(null);
        if (serviceGroup == null){
            return makeErrorResponse(ErrorCode.SERVICE_ERROR_NOT_FOUND, "Not found service group");
        }
        List<Service> services = serviceRepository.findAllByServiceGroupId(id);
        ServiceGroupAdminDto serviceGroupAdminDto = serviceGroupMapper.fromEntityToServiceGroupAdminDto(serviceGroup);
        serviceGroupAdminDto.setServices(serviceMapper.fromEntityListToServiceDtoListForServiceGroup(services));
        return makeSuccessResponse(serviceGroupAdminDto, "Get service group success");
    }

    @GetMapping(value = "/list", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_G_L')")
    public ApiMessageDto<ResponseListDto<List<ServiceGroupAdminDto>>> list(ServiceGroupCriteria serviceGroupCriteria, Pageable pageable) {
        if (serviceGroupCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<ServiceGroup> serviceGroups = serviceGroupRepository.findAll(serviceGroupCriteria.getCriteria(), pageable);
        ResponseListDto<List<ServiceGroupAdminDto>> responseListObj = new ResponseListDto<>();
        List<ServiceGroupAdminDto> serviceGroupAdminDtoList = serviceGroupMapper.fromEntityToServiceGroupAdminDtoList(serviceGroups.getContent());
        responseListObj.setContent(serviceGroupAdminDtoList);
        responseListObj.setTotalPages(serviceGroups.getTotalPages());
        responseListObj.setTotalElements(serviceGroups.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list service group success");
    }
    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<ServiceGroupDto>>> autoComplete(ServiceGroupCriteria serviceGroupCriteria) {
        Pageable pageable = serviceGroupCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        serviceGroupCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<ServiceGroup> serviceGroups = serviceGroupRepository.findAll(serviceGroupCriteria.getCriteria(), pageable);
        ResponseListDto<List<ServiceGroupDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(serviceGroupMapper.fromEntityToServiceGroupDtoList(serviceGroups.getContent()));
        responseListObj.setTotalPages(serviceGroups.getTotalPages());
        responseListObj.setTotalElements(serviceGroups.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list transaction group success");
    }
    @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_G_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateServiceGroupForm createServiceGroupForm, BindingResult bindingResult) {
        ServiceGroup serviceGroup = serviceGroupMapper.fromCreateServiceGroupFormToEntity(createServiceGroupForm);
        ServiceGroup serviceGroupByName = serviceGroupRepository.findFirstByName(serviceGroup.getName()).orElse(null);
        if (serviceGroupByName != null){
            return makeErrorResponse(ErrorCode.TRANSACTION_GROUP_ERROR_NAME_EXISTED, "Name service group existed");
        }
        serviceGroupRepository.save(serviceGroup);
        return makeSuccessResponse(null, "Create service group success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_G_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateServiceGroupForm updateServiceGroupForm, BindingResult bindingResult) {
        ServiceGroup serviceGroup = serviceGroupRepository.findById(updateServiceGroupForm.getId()).orElse(null);
        if (serviceGroup == null){
            return makeErrorResponse(ErrorCode.SERVICE_GROUP_ERROR_NOT_FOUND, "Not found service group");
        }
        if(!serviceGroup.getName().equals(updateServiceGroupForm.getName())){
            ServiceGroup serviceGroupByName = serviceGroupRepository.findFirstByName(updateServiceGroupForm.getName()).orElse(null);
            if (serviceGroupByName != null){
                return makeErrorResponse(ErrorCode.SERVICE_ERROR_NAME_EXISTED, "Name service group existed");
            }
        }
        serviceGroupMapper.fromUpdateServiceGroupFormToEntity(updateServiceGroupForm, serviceGroup);
        serviceGroupRepository.save(serviceGroup);
        return makeSuccessResponse(null, "Update service group success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_G_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        ServiceGroup serviceGroup = serviceGroupRepository.findById(id).orElse(null);
        if (serviceGroup == null){
            return makeErrorResponse(ErrorCode.TRANSACTION_GROUP_ERROR_NOT_FOUND, "Not found service group");
        }
        serviceRepository.updateAllByServiceGroupId(id);
        serviceGroupRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete service group success");
    }
}
