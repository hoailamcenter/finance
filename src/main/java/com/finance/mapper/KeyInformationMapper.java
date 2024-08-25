package com.finance.mapper;


import com.finance.dto.keyInformation.KeyInformationAdminDto;
import com.finance.dto.keyInformation.KeyInformationDto;
import com.finance.form.keyInformation.CreateKeyInformationForm;
import com.finance.form.keyInformation.UpdateKeyInformationForm;
import com.finance.model.KeyInformation;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AccountMapper.class, KeyInformationGroupMapper.class})
public interface KeyInformationMapper {

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "additionalInformation", target = "additionalInformation")
    @BeanMapping(ignoreByDefault = true)
    KeyInformation fromCreateKeyInformationFormToEntity(CreateKeyInformationForm createKeyInformationForm);

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "additionalInformation", target = "additionalInformation")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateKeyInformationFormToEntity(UpdateKeyInformationForm updateKeyInformationForm, @MappingTarget KeyInformation keyInformation);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoForNotificationGroup")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "additionalInformation", target = "additionalInformation")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToKeyInformationAdminDto")
    KeyInformationAdminDto fromEntityToKeyInformationAdminDto(KeyInformation keyInformation);

    @IterableMapping(elementTargetType = KeyInformationAdminDto.class, qualifiedByName = "fromEntityToKeyInformationAdminDto")
    List<KeyInformationAdminDto> fromEntityToKeyInformationAdminDtoList(List<KeyInformation> keyInformationList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoForNotificationGroup")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "additionalInformation", target = "additionalInformation")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToKeyInformationDto")
    KeyInformationDto fromEntityToKeyInformationDto(KeyInformation keyInformation);

    @IterableMapping(elementTargetType = KeyInformationDto.class, qualifiedByName = "fromEntityToKeyInformationDto")
    List<KeyInformationDto> fromEntityToKeyInformationDtoList(List<KeyInformation> keyInformationList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToKeyInformationDtoAutoComplete")
    KeyInformationDto fromEntityToKeyInformationDtoAutoComplete(KeyInformation keyInformation);

    @IterableMapping(elementTargetType = KeyInformationDto.class, qualifiedByName = "fromEntityToKeyInformationDtoAutoComplete")
    List<KeyInformationDto> fromEntityListToKeyInformationDtoAutoCompleteList(List<KeyInformation> keyInformations);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoForNotificationGroup")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "additionalInformation", target = "additionalInformation")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToKeyInformationDtoForGroup")
    KeyInformationDto fromEntityToKeyInformationDtoForGroup(KeyInformation keyInformation);

    @IterableMapping(elementTargetType = KeyInformationDto.class, qualifiedByName = "fromEntityToKeyInformationDtoForGroup")
    List<KeyInformationDto> fromEntityListToKeyInformationDtoListForGroup(List<KeyInformation> keyInformations);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "account", target = "account", qualifiedByName = "fromEntityToAccountDtoForNotificationGroup")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "additionalInformation", target = "additionalInformation")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToKeyInformationDtoForOrganization")
    KeyInformationDto fromEntityToKeyInformationDtoForOrganization(KeyInformation keyInformation);

    @IterableMapping(elementTargetType = KeyInformationDto.class, qualifiedByName = "fromEntityToKeyInformationDtoForOrganization")
    List<KeyInformationDto> fromEntityListToKeyInformationDtoListForOrganization(List<KeyInformation> keyInformations);
}
