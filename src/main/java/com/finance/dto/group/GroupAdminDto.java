package com.finance.dto.group;

import com.finance.dto.ABasicAdminDto;
import com.finance.dto.permission.PermissionDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GroupAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "permissions")
    private List<PermissionDto> permissions ;
}
