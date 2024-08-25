package com.finance.mapper;

import com.finance.dto.keyInformantionGroup.KeyInformationGroupAdminDto;
import com.finance.dto.keyInformantionGroup.KeyInformationGroupDto;
import com.finance.form.keyInformationGroup.CreateKeyInformationGroupForm;
import com.finance.form.keyInformationGroup.UpdateKeyInformationGroupForm;
import com.finance.model.KeyInformationGroup;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface KeyInformationGroupMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    KeyInformationGroup fromCreateKeyInformationGroupFormToEntity(CreateKeyInformationGroupForm createKeyInformationGroupForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdateKeyInformationGroupFormToEntity(UpdateKeyInformationGroupForm updateKeyInformationGroupForm, @MappingTarget KeyInformationGroup keyInformationGroup);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToKeyInformationGroupAdminDto")
    KeyInformationGroupAdminDto fromEntityToKeyInformationGroupAdminDto(KeyInformationGroup keyInformationGroup);

    @IterableMapping(elementTargetType = KeyInformationGroupAdminDto.class, qualifiedByName = "fromEntityToKeyInformationGroupAdminDto")
    List<KeyInformationGroupAdminDto> fromEntityListToKeyInformationGroupAdminDtoList(List<KeyInformationGroup> keyInformationGroups);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToKeyInformationGroupDtoAutoComplete")
    KeyInformationGroupDto fromEntityToKeyInformationGroupDtoAutoComplete(KeyInformationGroup keyInformationGroup);

    @IterableMapping(elementTargetType = KeyInformationGroupDto.class, qualifiedByName = "fromEntityToKeyInformationGroupDtoAutoComplete")
    List<KeyInformationGroupDto> fromEntityListToKeyInformationGroupDtoAutoCompleteList(List<KeyInformationGroup> keyInformationGroups);
}
