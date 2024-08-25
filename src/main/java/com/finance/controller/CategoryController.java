package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.category.CategoryAdminDto;
import com.finance.form.category.CreateCategoryForm;
import com.finance.form.category.UpdateCategoryForm;
import com.finance.mapper.CategoryMapper;
import com.finance.model.Category;
import com.finance.model.criteria.CategoryCriteria;
import com.finance.repository.*;
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
@RequestMapping("/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoryController extends ABasicController{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_V')")
    public ApiMessageDto<CategoryAdminDto> get(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not found category");
        }
        return makeSuccessResponse(categoryMapper.fromEntityToCategoryAdminDto(category), "Get department success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CA_L')")
    public ApiMessageDto<ResponseListDto<List<CategoryAdminDto>>> list(CategoryCriteria categoryCriteria, Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(categoryCriteria.getCriteria(), pageable);
        ResponseListDto<List<CategoryAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(categoryMapper.fromEntityListToCategoryAdminDtoList(categories.getContent()));
        responseListObj.setTotalPages(categories.getTotalPages());
        responseListObj.setTotalElements(categories.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list category success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<CategoryAdminDto>>> autoComplete(CategoryCriteria categoryCriteria) {
        Pageable pageable = PageRequest.of(0,10);
        categoryCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<Category> categories = categoryRepository.findAll(categoryCriteria.getCriteria(), pageable);
        ResponseListDto<List<CategoryAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(categoryMapper.fromEntityListToCategoryAdminDtoList(categories.getContent()));
        responseListObj.setTotalPages(categories.getTotalPages());
        responseListObj.setTotalElements(categories.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list category success");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DE_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCategoryForm createCategoryForm, BindingResult bindingResult) {
        Category categoryByName = categoryRepository.findFirstByNameAndKind(createCategoryForm.getName(), createCategoryForm.getKind()).orElse(null);
        if(categoryByName != null){
            return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NAME_EXISTED, "Name existed");
        }
        Category category = categoryMapper.fromCreateCategoryFormToEntity(createCategoryForm);
        categoryRepository.save(category);
        return makeSuccessResponse(null, "Create category success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DE_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCategoryForm updateCategoryForm, BindingResult bindingResult) {
        Category category = categoryRepository.findById(updateCategoryForm.getId()).orElse(null);
        if (category == null){
            return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not found category");
        }
        if(!category.getName().equals(updateCategoryForm.getName())){
            Category categoryByName = categoryRepository.findFirstByNameAndKind(updateCategoryForm.getName(),category.getKind()).orElse(null);
            if(categoryByName != null){
                return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NAME_EXISTED, "Name existed");
            }
        }
        categoryMapper.fromUpdateCategoryFormToEntity(updateCategoryForm, category);
        categoryRepository.save(category);
        return makeSuccessResponse(null, "Update department success");
    }

    @Transactional
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DE_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null){
            return makeErrorResponse(ErrorCode.DEPARTMENT_ERROR_NOT_FOUND, "Not found department");
        }
        categoryRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete department success");
    }
}
