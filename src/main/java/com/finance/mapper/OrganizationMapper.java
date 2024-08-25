package com.finance.mapper;

import com.finance.dto.organization.OrganizationAdminDto;
import com.finance.dto.organization.OrganizationDto;
import com.finance.form.organization.CreateOrganizationForm;
import com.finance.form.organization.UpdateOrganizationForm;
import com.finance.model.Organization;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrganizationMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "logo", target = "logo")
    @BeanMapping(ignoreByDefault = true)
    Organization fromCreateOrganizationFormToEntity(CreateOrganizationForm createOrganizationForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "logo", target = "logo")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateOrganizationFormToEntity(UpdateOrganizationForm updateOrganizationForm, @MappingTarget Organization organization);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "logo", target = "logo")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToKeyInformationGroupAdminDto")
    OrganizationAdminDto fromEntityToOrganizationAdminDto(Organization organization);

    @IterableMapping(elementTargetType = OrganizationAdminDto.class, qualifiedByName = "fromEntityToOrganizationAdminDto")
    List<OrganizationAdminDto> fromEntityListToOrganizationAdminDtoList(List<Organization> organizations);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToOrganizationDto")
    OrganizationDto fromEntityToOrganizationDto(Organization organization);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToOrganizationDtoAutoComplete")
    OrganizationDto fromEntityToOrganizationDtoAutoComplete(Organization organization);

    @IterableMapping(elementTargetType = OrganizationDto.class, qualifiedByName = "fromEntityToOrganizationDtoAutoComplete")
    List<OrganizationDto> fromEntityListToOrganizationDtoAutoCompleteList(List<Organization> organizations);
}
