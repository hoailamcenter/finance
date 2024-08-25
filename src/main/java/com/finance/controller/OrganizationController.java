package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.organization.OrganizationAdminDto;
import com.finance.dto.organization.OrganizationDto;
import com.finance.form.organization.CreateOrganizationForm;
import com.finance.form.organization.UpdateOrganizationForm;
import com.finance.mapper.KeyInformationMapper;
import com.finance.mapper.OrganizationMapper;
import com.finance.model.KeyInformation;
import com.finance.model.Organization;
import com.finance.model.criteria.OrganizationCriteria;
import com.finance.repository.KeyInformationRepository;
import com.finance.repository.OrganizationRepository;
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
@RequestMapping("/v1/organization")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrganizationController extends ABasicController {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private KeyInformationRepository keyInformationRepository;
    @Autowired
    private KeyInformationMapper keyInformationMapper;
    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OR_V')")
    public ApiMessageDto<OrganizationAdminDto> get(@PathVariable("id") Long id) {
        Organization organization = organizationRepository.findById(id).orElse(null);
        if (organization == null) {
            return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NOT_FOUND, "Not found organization");
        }
        OrganizationAdminDto organizationAdminDto = organizationMapper.fromEntityToOrganizationAdminDto(organization);
        List<KeyInformation> keyInformations = keyInformationRepository.findByOrganizationId(organization.getId());
        organizationAdminDto.setKeyInformations(keyInformationMapper.fromEntityListToKeyInformationDtoListForOrganization(keyInformations));
        return makeSuccessResponse(organizationAdminDto, "Get organization success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OR_L')")
    public ApiMessageDto<ResponseListDto<List<OrganizationAdminDto>>> list(OrganizationCriteria organizationCriteria, Pageable pageable) {
        if (organizationCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<Organization> organizations = organizationRepository.findAll(organizationCriteria.getCriteria(), pageable);
        List<OrganizationAdminDto> organizationAdminDtos = organizationMapper.fromEntityListToOrganizationAdminDtoList(organizations.getContent());
        ResponseListDto<List<OrganizationAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(organizationAdminDtos);
        responseListObj.setTotalPages(organizations.getTotalPages());
        responseListObj.setTotalElements(organizations.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list organization success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<OrganizationDto>>> autoComplete(OrganizationCriteria organizationCriteria) {
        Pageable pageable = organizationCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        organizationCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<Organization> organizations = organizationRepository.findAll(organizationCriteria.getCriteria(), pageable);
        ResponseListDto<List<OrganizationDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(organizationMapper.fromEntityListToOrganizationDtoAutoCompleteList(organizations.getContent()));
        responseListObj.setTotalPages(organizations.getTotalPages());
        responseListObj.setTotalElements(organizations.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list organization success");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OR_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateOrganizationForm createOrganizationForm, BindingResult bindingResult) {
        Organization organizationByName = organizationRepository.findFirstByName(createOrganizationForm.getName()).orElse(null);
        if(organizationByName != null){
            return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NAME_EXISTED, "Name existed");
        }
        Organization organization = organizationMapper.fromCreateOrganizationFormToEntity(createOrganizationForm);
        organizationRepository.save(organization);
        return makeSuccessResponse(null, "Create organization success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OR_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateOrganizationForm updateOrganizationForm, BindingResult bindingResult) {
        Organization organization = organizationRepository.findById(updateOrganizationForm.getId()).orElse(null);
        if (organization == null) {
            return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NOT_FOUND, "Not found organization group");
        }
        if(!organization.getName().equals( updateOrganizationForm.getName())){
            Organization organizationByName = organizationRepository.findFirstByName(updateOrganizationForm.getName()).orElse(null);
            if(organizationByName != null){
                return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NAME_EXISTED, "Name existed");
            }
        }
        organizationMapper.fromUpdateOrganizationFormToEntity(updateOrganizationForm, organization);
        organizationRepository.save(organization);
        return makeSuccessResponse(null, "Update organization success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('OR_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Organization organization = organizationRepository.findById(id).orElse(null);
        if (organization == null) {
            return makeErrorResponse(ErrorCode.ORGANIZATION_ERROR_NOT_FOUND, "Not found organization");
        }
        serviceRepository.updateAllByOrganizationId(id);
        keyInformationRepository.updateAllByOrganizationId(id);
        organizationRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete organization success");
    }
}
