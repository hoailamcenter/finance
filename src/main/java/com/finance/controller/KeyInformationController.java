package com.finance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.keyInformation.KeyInformationAdminDto;
import com.finance.dto.keyInformation.KeyInformationDto;
import com.finance.form.keyInformation.CreateKeyInformationForm;
import com.finance.form.keyInformation.UpdateKeyInformationForm;
import com.finance.mapper.KeyInformationMapper;
import com.finance.model.Account;
import com.finance.model.KeyInformation;
import com.finance.model.KeyInformationGroup;
import com.finance.model.Organization;
import com.finance.model.criteria.KeyInformationCriteria;
import com.finance.repository.AccountRepository;
import com.finance.repository.KeyInformationGroupRepository;
import com.finance.repository.KeyInformationRepository;
import com.finance.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/v1/key-information")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class KeyInformationController extends ABasicController{
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KeyInformationGroupRepository keyInformationGroupRepository;

    @Autowired
    private KeyInformationRepository keyInformationRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private KeyInformationMapper keyInformationMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('KE_I_L')")
    public ApiMessageDto<ResponseListDto<List<KeyInformationAdminDto>>> list(KeyInformationCriteria keyInformationCriteria, Pageable pageable) {
        Page<KeyInformation> keyInformations = keyInformationRepository.findAll(keyInformationCriteria.getCriteria(), pageable);
        ResponseListDto<List<KeyInformationAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(keyInformationMapper.fromEntityToKeyInformationAdminDtoList(keyInformations.getContent()));
        responseListObj.setTotalPages(keyInformations.getTotalPages());
        responseListObj.setTotalElements(keyInformations.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list key information success");
    }
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('SE_V')")
    public ApiMessageDto<KeyInformationAdminDto> get(@PathVariable Long id) {
        KeyInformation keyInformation = keyInformationRepository.findById(id).orElse(null);
        if (keyInformation == null) {
            makeErrorResponse(ErrorCode.KEY_INFORMATION_ERROR_NOT_FOUND, "Not found key information");
        }
        return makeSuccessResponse(keyInformationMapper.fromEntityToKeyInformationAdminDto(keyInformation), "Get key information success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<KeyInformationDto>>> autoComplete(KeyInformationCriteria keyInformationCriteria) {
        Pageable pageable = keyInformationCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        keyInformationCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<KeyInformation> informations = keyInformationRepository.findAll(keyInformationCriteria.getCriteria(), pageable);
        ResponseListDto<List<KeyInformationDto>> responseListObj = new ResponseListDto<>();
        List<KeyInformationDto> keyInformationDtos = keyInformationMapper.fromEntityListToKeyInformationDtoAutoCompleteList(informations.getContent());
        responseListObj.setContent(keyInformationDtos);
        responseListObj.setTotalPages(informations.getTotalPages());
        responseListObj.setTotalElements(informations.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list key information success");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('KE_I_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateKeyInformationForm createKeyInformationForm, BindingResult bindingResult) {
        Account account = accountRepository.findById(getCurrentUser()).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        KeyInformation keyInformation = keyInformationMapper.fromCreateKeyInformationFormToEntity(createKeyInformationForm);
        keyInformation.setAccount(account);
        if (createKeyInformationForm.getKeyInformationGroupId() != null) {
            KeyInformationGroup keyInformationGroup = keyInformationGroupRepository.findById(createKeyInformationForm.getKeyInformationGroupId()).orElse(null);
            if (keyInformationGroup == null) {
                return makeErrorResponse(ErrorCode.KEY_INFORMATION_GROUP_ERROR_NOT_FOUND, "Not found key information group");
            }
            keyInformation.setKeyInformationGroup(keyInformationGroup);
        }
        if (createKeyInformationForm.getOrganizationId() != null){
            Organization organization = organizationRepository.findById(createKeyInformationForm.getOrganizationId()).orElse(null);
            if (organization == null){
                return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NOT_FOUND, "Not found organization");
            }
            keyInformation.setOrganization(organization);
        }
        keyInformationRepository.save(keyInformation);
        return makeSuccessResponse(null, "Create key information success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('KE_I_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateKeyInformationForm updateKeyInformationForm, BindingResult bindingResult) {
        KeyInformation keyInformation = keyInformationRepository.findById(updateKeyInformationForm.getId()).orElse(null);
        if (keyInformation == null) {
            return makeErrorResponse(ErrorCode.KEY_INFORMATION_ERROR_NOT_FOUND, "Not found key information");
        }
        keyInformationMapper.fromUpdateKeyInformationFormToEntity(updateKeyInformationForm, keyInformation);
        if (updateKeyInformationForm.getKeyInformationGroupId() != null) {
            KeyInformationGroup keyInformationGroup = keyInformationGroupRepository.findById(updateKeyInformationForm.getKeyInformationGroupId()).orElse(null);
            if (keyInformationGroup == null) {
                return makeErrorResponse(ErrorCode.KEY_INFORMATION_GROUP_ERROR_NOT_FOUND, "Not found key information group");
            }
            keyInformation.setKeyInformationGroup(keyInformationGroup);
        } else {
            keyInformation.setKeyInformationGroup(null);
        }
        if (updateKeyInformationForm.getOrganizationId() != null){
            Organization organization = organizationRepository.findById(updateKeyInformationForm.getOrganizationId()).orElse(null);
            if (organization == null){
                return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NOT_FOUND, "Not found organization");
            }
            keyInformation.setOrganization(organization);
        } else {
            keyInformation.setOrganization(null);
        }
        keyInformationRepository.save(keyInformation);
        return makeSuccessResponse(null, "Update key information success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('KE_I_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        KeyInformation keyInformation = keyInformationRepository.findById(id).orElse(null);
        if (keyInformation == null) {
            return makeErrorResponse(ErrorCode.KEY_INFORMATION_ERROR_NOT_FOUND, "Not found key information");
        }
        keyInformationRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete key information success");
    }
}
