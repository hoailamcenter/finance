package com.finance.mapper;

import com.finance.dto.category.CategoryAdminDto;
import com.finance.dto.category.CategoryDto;
import com.finance.form.category.CreateCategoryForm;
import com.finance.form.category.UpdateCategoryForm;
import com.finance.model.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCategoryAdminDto")
    CategoryAdminDto fromEntityToCategoryAdminDto(Category category);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCategoryDto")
    CategoryDto fromEntityToCategoryDto(Category category);

    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    Category fromCreateCategoryFormToEntity(CreateCategoryForm createCategoryForm);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    void fromUpdateCategoryFormToEntity(UpdateCategoryForm updateCategoryForm, @MappingTarget Category category);

    @IterableMapping(elementTargetType = CategoryAdminDto.class, qualifiedByName = "fromEntityToCategoryAdminDto")
    List<CategoryAdminDto> fromEntityListToCategoryAdminDtoList(List<Category> categories);

    @IterableMapping(elementTargetType = CategoryDto.class, qualifiedByName = "fromEntityToCategoryDto")
    List<CategoryDto> fromEntityListToCategoryDtoList(List<Category> categories);
}
