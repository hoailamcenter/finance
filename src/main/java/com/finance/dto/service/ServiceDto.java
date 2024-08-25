package com.finance.dto.service;

import com.finance.dto.category.CategoryDto;
import com.finance.dto.serviceGroup.ServiceGroupDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

@Data
public class ServiceDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "money")
    private String money;
    @ApiModelProperty(name = "startDate")
    private Date startDate;
    @ApiModelProperty(name = "periodKind")
    private Integer periodKind;
    @ApiModelProperty(name = "expirationDate")
    private Date expirationDate;
    @ApiModelProperty(name = "category")
    private CategoryDto category;
    @ApiModelProperty(name = "serviceGroup")
    private ServiceGroupDto serviceGroup;
}
