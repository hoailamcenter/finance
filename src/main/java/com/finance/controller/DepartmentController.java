package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.department.DepartmentAdminDto;
import com.finance.dto.department.DepartmentDto;
import com.finance.form.department.CreateDepartmentForm;
import com.finance.form.department.UpdateDepartmentForm;
import com.finance.mapper.DepartmentMapper;
import com.finance.model.Department;
import com.finance.model.criteria.DepartmentCriteria;
import com.finance.repository.AccountRepository;
import com.finance.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/department")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class DepartmentController extends ABasicController{
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DepartmentMapper departmentMapper;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DE_V')")
    public ApiMessageDto<DepartmentAdminDto> get(@PathVariable("id") Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if(department == null){
            return makeErrorResponse(ErrorCode.DEPARTMENT_ERROR_NOT_FOUND, "Not found department");
        }
        return makeSuccessResponse(departmentMapper.fromEntityToDepartmentAdminDto(department), "Get department success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DE_L')")
    public ApiMessageDto<ResponseListDto<List<DepartmentAdminDto>>> list(DepartmentCriteria departmentCriteria, Pageable pageable) {
        Page<Department> departments = departmentRepository.findAll(departmentCriteria.getCriteria(), pageable);
        ResponseListDto<List<DepartmentAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(departmentMapper.fromEntityListToDepartmentAdminDtoList(departments.getContent()));
        responseListObj.setTotalPages(departments.getTotalPages());
        responseListObj.setTotalElements(departments.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list department success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<DepartmentDto>>> autoComplete(DepartmentCriteria departmentCriteria) {
        Pageable pageable = PageRequest.of(0,10);
        departmentCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<Department> departments = departmentRepository.findAll(departmentCriteria.getCriteria(), pageable);
        ResponseListDto<List<DepartmentDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(departmentMapper.fromEntityListToDepartmentDtoList(departments.getContent()));
        responseListObj.setTotalPages(departments.getTotalPages());
        responseListObj.setTotalElements(departments.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list department success");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DE_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateDepartmentForm createDepartmentForm, BindingResult bindingResult) {
        Department departmentByName = departmentRepository.findFirstByName(createDepartmentForm.getName()).orElse(null);
        if(departmentByName != null){
            return makeErrorResponse(ErrorCode.DEPARTMENT_ERROR_NAME_EXISTED, "Name existed");
        }
        Department department = departmentMapper.fromCreateDepartmentFormToEntity(createDepartmentForm);
        departmentRepository.save(department);
        return makeSuccessResponse(null, "Create department success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DE_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateDepartmentForm updateDepartmentForm, BindingResult bindingResult) {
        Department department = departmentRepository.findById(updateDepartmentForm.getId()).orElse(null);
        if (department == null){
            return makeErrorResponse(ErrorCode.DEPARTMENT_ERROR_NOT_FOUND, "Not found department");
        }
        if(!department.getName().equals(updateDepartmentForm.getName())){
            Department departmentByName = departmentRepository.findFirstByName(updateDepartmentForm.getName()).orElse(null);
            if(departmentByName != null){
                return makeErrorResponse(ErrorCode.DEPARTMENT_ERROR_NAME_EXISTED, "Name existed");
            }
        }
        departmentMapper.fromUpdateDepartmentFormToEntity(updateDepartmentForm, department);
        departmentRepository.save(department);
        return makeSuccessResponse(null, "Update department success");
    }

    @Transactional
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DE_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if(department == null){
            return makeErrorResponse(ErrorCode.DEPARTMENT_ERROR_NOT_FOUND, "Not found department");
        }
        accountRepository.updateAllByDepartmentId(id);
        departmentRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete department success");
    }
}
